var script = document.createElement('script');
script.src = 'https://code.jquery.com/jquery-3.4.1.min.js';
script.type = 'text/javascript';
document.getElementsByTagName('head')[0].appendChild(script);

function initMap() {
  const map = new google.maps.Map(document.getElementById("map"), {
    zoom: 3,
    center: { lat:  39.921, lng: 5.998 },
  });
  const geocoder = new google.maps.Geocoder();

  // Gets latlng from click event
  map.addListener("click", (mapsMouseEvent) => {
    let cordi = mapsMouseEvent.latLng.toJSON()
    geocodeLatLng(geocoder, map, cordi);
  });

  map.setOptions({ minZoom: 2, maxZoom: 6 });
}


function geocodeLatLng(geocoder, map, cordi) {
  console.log(cordi.lat)
  console.log(cordi.lng)
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
            let country = results[0].address_components[index].long_name

            getWeather(cordi.lat,cordi.lng,country);
            // Skapar ett objekt med countryName

            var data = {};
            let playlistID;
            data.countryName = country;

            function sendText(information) {
              console.log(information)
              wikiInfo.innerHTML=information
            }
            function sendPlaylist(id) {
                  iframe.src = "https://open.spotify.com/embed/playlist/" + id
                  iframe.allow = "encrypted-media"
                }

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

                // Wikipedia GET API call
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
                infographic.style.display = "block"
                let map = document.getElementById("map")
                map.style.height = "40%"
                let wikiInfo = document.getElementById("wikiInfo")

                // Kontrollerar om Spotify spelaren ej visas.
                // if (infographic.style.display === "none") {
                //   infographic.style.display = "block"
                // }

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

weather = document.getElementById("weather")
let weatherFrame = document.getElementById("weatherFrame")

function getWeather(lat,lon, country) {

  $.getJSON("http://api.openweathermap.org/data/2.5/weather?lat=" + lat +"&lon=" + lon + "&units=metric&appid=ccb56b0d59812fced4e6355440154d34",function(json){
  // console.log(JSON.stringify(json))
  let temp = json.main.temp.toFixed(0)
  let iconcode = json.weather[0].icon
  let region = json.name

  console.log(iconcode)
  console.log(region)
  console.log(json.main.temp + " celcius grader")

  // Resets temperature info + creates new img element for weather icon
  document.getElementById("temp").innerHTML=""
  let ImgElement = document.createElement('img');
  ImgElement.id="weatherFrame"
  document.getElementById("temp").appendChild(ImgElement);

  weatherFrame = document.getElementById("weatherFrame")
  weatherFrame.src="http://openweathermap.org/img/w/" + iconcode + ".png";
  document.getElementById("weatherInfo").innerHTML = temp + "\n" + region + "," + country;
  document.getElementById("temp").innerHTML += temp + "°C";
  });
}




// {"coord":{"lon":14.359,"lat":59.2932},
// "weather":[{"id":804,"main":"Clouds","description":"overcast clouds","icon":"04n"}],
// "base":"stations",
// "main":{"temp":271.31,"feels_like":264.73,"temp_min":269.82,"temp_max":272.15,"pressure":1022,"humidity":80},
// "visibility":10000,
// "wind":{"speed":5.7,"deg":50},
// "clouds":{"all":97},
// "dt":1609945618,
// "sys":{"type":1,"id":1777,"country":"SE","sunrise":1609919713,"sunset":1609942875},
// "timezone":3600,
// "id":2717881,
// "name":"Degerfors Kommun",
// "cod":200}
