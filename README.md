<h1 align="center">
  <br>
  Grocery Store MVVM - DI
  <br>
</h1>

<p align="center">
 <a href="#project-overview">Project Overview</a> •
 <a href="#setup-instructions">Setup Instructions</a> •
 <a href="#apk-build-instructions">APK build instructions</a> •
 <a href="#source-code">Source Code</a> •
 <a href="#released-apk">Released Apk</a>
</p>

## Project Overview
The project contains a login and a sign-up screen. Upon successfully signing up or logging in, the user is navigated to the dashboard, which displays a list of grocery items. The user can add items to their cart.

The toolbar contains three menu items:

- A search option to look for grocery items in the table.
- A My Cart option that navigates the user to the My Cart screen.
- A dotted menu that opens a popup with a Logout option.

Clicking on My Cart navigates the user to the My Cart screen, where they can see a list of added items, along with a Checkout button. Clicking Checkout brings up a bottom sheet to proceed with payment.

Upon selecting a payment option, the payment page is opened, and all items in the cart are removed as their order status is set to true, indicating that the order has been successfully placed.

## Setup Instructions

 - Clone the project on your system.
```bash
git clone https://github.com/thisisvd/Grocery-Store-MVVM
```
- Sync the project -> Clean Project -> then build it and run it on your appropiate android device.

## APK build instructions
**Create Debug build**
 - Select the `build varient` (debug).
 - Select `Build` in top menu -> then `Build Apk Bundle(s) / Apk(s)` -> then `Build Apk`.
   
**Create Release build**
 - Select the `build varient` (release).
 - Select `Build` in top menu -> then `Generate Signed Apk Bundle` -> then `Select Apk or bundle` -> then add .jks key present at main root folder with key alias `grocery` & password `grocery` (Shared only for testing purpose else never share .jks keys) -> Locate build.
 - Note: To run release build you have to edit congfiguration and set signin config to debug to successful run of release build in emulator or connected device. 
  
## Source Code
- `Tech-stack used:` Java, DI(Hilt), ROOM, Navigation, Timber etc. 

## Released Apk
- Released Application path can be found [here!](https://github.com/thisisvd/Grocery-Store-MVVM/blob/master/app/release/app-release.apk).
