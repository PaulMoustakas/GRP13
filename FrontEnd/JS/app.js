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
            console.log(results)
            console.log(results[0])
            if (results[0]) {
            // Loops trough each type until type of country find.
            for (let index = 0; index < results[0].address_components.length; index++) {
                if (results[0].address_components[index].types[0] == "country") {
                    let country = results[0].address_components[index].short_name
                    console.log("Top 50 i " + country)
                      if (player.style.display === "none") {
                        player.style.display = "block"
                      }
                      console.log("Successssss")
                      player = document.getElementById("player")
                      let iframe = document.getElementById("iframe")

                      let SE = "46sPNZPmAAGf40agv6QUJm"
                      let ES = "37i9dQZEVXbNFJfN1Vw8d9"
                      
                      if (country == "SE") {
                      iframe.src = "https://open.spotify.com/embed/album/" + SE
                      }
                      else if (country == "ES") {
                      iframe.src = "https://open.spotify.com/embed/playlist/" + ES
                      }
                      
                      iframe.allowtransparency = "true"
                      iframe.allow = "encrypted-media"
                      iframe.width="400" 
                      iframe.height="380" 
                      iframe.frameborder="2px"
                  
                    return country 
                    // Send country to spotify API
                    // check if result is empty, then return Pirate Playlist
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
