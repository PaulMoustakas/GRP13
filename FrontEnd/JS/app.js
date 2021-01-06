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

    console.log(cordi.lat + " " + cordi.lng)
    getWeather(cordi.lat,cordi.lng);
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
            
            // Skapar ett objekt med countryName 
            var data = {};
            data.countryName = country;

            $.ajax({
              method: "POST",
              url: 'http://localhost:3000/country',
              data: JSON.stringify(data),
              headers: {"Accept": "application/json"}
            })
            .done(function(result) {
            });
            
            //Ajax func för att hämta playlist id --> ID



            player = document.getElementById("player")
            let iframe = document.getElementById("iframe")

            // Kontrollerar om Spotify spelaren ej visas.
            if (player.style.display === "none") {
              player.style.display = "block"
            }

            // Playlist-ID från Backend
            // let playlistID = ""
            // iframe.src = "https://open.spotify.com/embed/album/" + playlistID

            let SE = "46sPNZPmAAGf40agv6QUJm"
            let ES = "37i9dQZEVXbNFJfN1Vw8d9"

            if (country == "Sverige") {
            iframe.src = "https://open.spotify.com/embed/album/" + SE
            }
            else if (country == "Spanien") {
            iframe.src = "https://open.spotify.com/embed/playlist/" + ES
            }

            // iframe.allowtransparency = "true"  //Nödvändig?
            iframe.allow = "encrypted-media"
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

function getWeather(lat,lon) {
  $.getJSON("http://api.openweathermap.org/data/2.5/weather?lat=55&lon=13&appid=da1628d2e0d5876be7710a4cecd3b03d",function(json){
  console.log(JSON.stringify(json))    
  });

}

// http://api.openweathermap.org/data/2.5/weather?lat=55&lon=13&appid=da1628d2e0d5876be7710a4cecd3b03d