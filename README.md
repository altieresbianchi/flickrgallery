# Flickr Gallery - Android
Flickr Gallery is an app created for the purpose of presenting knowledge in building Android applications with the Kotlin language.\
There is no commercial goal in this development.

# Architecture:
MVVM - Model View ViewModel

![MVVM Architecture](http://www.phaneronsoft.com.br/wp-content/uploads/2021/03/MVVM_Architecture.png?raw=true "Architecture MVVM")

# Applied Techniques
In this app, some development techniques were used as described below:
- Language: Kotlin with Coroutines
- Architecture: MVVM
- XML Mapping: View Binding
- Dependency Injection: Koin
- HTTP requests: Retrofit
- Unit Tests: JUnit
- Mock for unit tests: Mockito

# API
For this app, the API used was: https://api.flickr.com/services/rest.

*Flickr API* The Flickr API is available for non-commercial use by external developers. Commercial use is possible by prior arrangement.

# Features
The app has one screen to present a gallery of images. when scrolling down, more images are displayed. 
Touching in one of the images displays the large image with description and arrows to navigation between the images.
