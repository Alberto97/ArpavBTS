# Arpa Veneto BTS
<img width="30%" vspace="20" src="http://alberto97.altervista.org/arpav/assets/Screenshot_1.png"> <img width="30%" vspace="20" src="http://alberto97.altervista.org/arpav/assets/Screenshot_2.png"> <img width="30%" vspace="20" src="http://alberto97.altervista.org/arpav/assets/Screenshot_3.png">

## Setup
In order to properly run the app, you'll have to:

1. [Get a Maps API key](https://developers.google.com/maps/documentation/android-sdk/get-api-key)
1. Create a file in the `app` directory called `secure.properties` (this file should *NOT* be under version control to protect your API key)
1. Add a single line to `app/secure.properties` that looks like `GOOGLE_MAPS_API_KEY=YOUR_API_KEY`, where `YOUR_API_KEY` is the API key you obtained in the first step
1. Build and run
