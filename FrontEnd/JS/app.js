var script = document.createElement('script');
script.src = 'https://code.jquery.com/jquery-3.4.1.min.js';
script.type = 'text/javascript';
document.getElementsByTagName('head')[0].appendChild(script);

function initMap() {
  // Creates the map and visualises it
  const map = new google.maps.Map(document.getElementById("map"), {
    zoom: 3,
    center: { lat:  39.921, lng: 5.998 },
  });
  const geocoder = new google.maps.Geocoder();

  // Gets latlng from click event
  map.addListener("click", (mapsMouseEvent) => {
    let cordi = mapsMouseEvent.latLng.toJSON()
    /// Function call with map, geocoder & coordinates in parameter
    geocodeLatLng(geocoder, map, cordi);
  });
  
  // Inital start zoom on map
  map.setOptions({ minZoom: 2, maxZoom: 6 });
}


function geocodeLatLng(geocoder, map, cordi) {
 // Creates latlng obj with coordinates
  const latlng = {
    lat: cordi.lat,
    lng: cordi.lng,
  };

  geocoder.geocode({ location: latlng }, (results, status) => {
    if (status === "OK") {
      if (results[0]){
      // Loops trough each type until type country is find.
      for (let index = 0; index < results[0].address_components.length; index++) {
        if (results[0].address_components[index].types[0] == "country") {
           // Saves longname for country in variable
            let country = results[0].address_components[index].long_name

            getWeather(cordi.lat,cordi.lng,country);
            
            // Creates an object with countryName
            var data = {};
            data.countryName = country;

            function sendText(information) {
                wikiInfo.innerHTML= information
            }

            function sendPlaylist(id) {
              // Add Playlist ID to Iframe source
                iframe.src = "https://open.spotify.com/embed/playlist/" + id
                iframe.allow = "encrypted-media"
            }

            // API call for Playlist ID for a specific country
            $.ajax({
              method: "GET",
              url: 'http://localhost:3000/playlist/' + data.countryName,
              data: JSON.stringify(data),
              headers: {"Accept": "application/json"},

              success: function() {
              console.log('AJAX CALL succesfull');
            },

              error: function() {
                console.error('Ajax call failed');

            }
            })

            .done(function(result) {
              let playlistID = result.top50Playlist;
              sendPlaylist(playlistID);
            });

            // API call for information text about a specific country
            $.ajax({
              method: "GET",
              url: 'http://localhost:3000/information/' + data.countryName,
              data: JSON.stringify(data),
              headers: {"Accept": "application/json"},

              success: function() {
              console.log('AJAX CALL succesfull');
            },

            error: function() {
              console.error('Ajax call failed');

            }
            })

            .done(function(result) {
              console.log(result.wikiText)
              let information = result.wikiText;
              sendText(information);
            });
            

            
            let infographic = document.getElementById("infographic")
            let iframe = document.getElementById("iframe")
            let wikiInfo = document.getElementById("wikiInfo")
            let map = document.getElementById("map")

            // Shows infographic on map-clickevent
            infographic.style.display = "block"
            
            // Changes map height on map-clickevent 
            map.style.height = "40%"
        }
      }
      }
      else {
        window.alert("No results found");
      }
    } else {
      window.alert("Geocoder failed due to: " + status);
    }
  });
}


let weather = document.getElementById("weather")
let weatherFrame = document.getElementById("weatherFrame")

function getWeather(lat,lon, country) {

  // Webbased API call on openweather with coordinates from map-clickevent 
  $.getJSON("http://api.openweathermap.org/data/2.5/weather?lat=" + lat +"&lon=" + lon + "&units=metric&appid=ccb56b0d59812fced4e6355440154d34",function(result){
  let temp = result.main.temp.toFixed(0)
  let iconcode = result.weather[0].icon
  let region = result.name

  // Resets temperature info + creates new img element for weather icon
  document.getElementById("temp").innerHTML=""
  let ImgElement = document.createElement('img');
  ImgElement.id="weatherFrame"
  document.getElementById("temp").appendChild(ImgElement);

  weatherFrame = document.getElementById("weatherFrame")
  weatherFrame.src="http://openweathermap.org/img/w/" + iconcode + ".png";
  document.getElementById("weatherInfo").innerHTML = region + "," + country;
  document.getElementById("temp").innerHTML += temp + "°C";
  });
}