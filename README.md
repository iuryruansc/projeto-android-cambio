# Welcome to the project Currency Converter

This Android application allows users to convert between various currencies.

## Features

* **Comprehensive Currency List:**  Displays a menu of available currencies for selection.
* **Real-time Conversion:**  Provides instant conversions of 1 unit of the selected currency to other currencies.
* **User-friendly Interface:**  Designed for easy navigation and intuitive use.

## Dependencies

* **Retrofit (2.9.0):**  For making network requests to fetch currency exchange rates.
* **Gson (2.9.0):**  For parsing JSON data from the API.
* **Kotlin Coroutines:**  For managing asynchronous operations.
* **AndroidX ViewModel:**  For managing UI state and data.

## API Documentation

**Exchange Rates Data**

* **Endpoints Used:**
  <br>/latest
  <br>/symbols
  <br>
* **Request Parameters:**
  <br> /symbols: only the API Key
  <br> /latest: @base (a three letter currency code of the selected currency) and the API Key
  <br>
* **Response Format:**
  <br> /symbols:
   ```json
   {
     "success": true,
     "symbols": {
       "AED": "United Arab Emirates Dirham",
       "AFN": "Afghan Afghani",
       "ALL": "Albanian Lek",
       "AMD": "Armenian Dram"
     }
   }
   ````
   <br>/latest
   ```json
  {
  "base": "USD",
  "date": "2021-03-17",
  "rates": {
    "EUR": 0.813399,
    "GBP": 0.72007,
    "JPY": 107.346001
  },
  "success": true,
  "timestamp": 1519296206
   }

## Installation and Setup

1. **Clone the repository:**
   ```bash
   git clone https://github.com/iuryruansc/projeto-android-cambio.git

2. **Open the project in your IDE:**
   <br>Android Studio is recommended.

3. **Run the application:**
   <br>Ensure your device or emulator is running Android 7.0 or higher.
   <br>Click the "Run" button in your IDE.

## Usage

1. Launch the app. <br>
2. Select a currency from the menu. <br>
3. View the equivalent values of 1 unit of the selected currency in other currencies. <br>

## Screenshots
| <img src="./assets/project_wm_1.png" height="450" alt="initial interface screenshot"/> | <img src="./assets/project_wm_2.png" height="450" alt="menu list screenshot"/> | <img src="./assets/project_wm_3.png" height="450" alt="converted currency screenshot"/> |
|:---:| :---: | :---: |
