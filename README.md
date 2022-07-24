# PixabayPayback
This is an app which use Pixabay api to search images. I built this app following MVVM, Clean and Single Activity Architecture


# Project Architecture
### Communication between layers
1. Views invoke methods from ViewModel.
2. ViewModel executes use case.
3. Use case executes one or multiple repository function.
4. The Repository returns data from one or multiple Data Sources. the repository is the single source of truth
5. Information flows back to the view where we display the data fetched from data sources.

This app contains three layers(data, domain , presentation)

# Project Structure
* Data
    * This is my data layer and consisted of the Network
    related classes including the ImageApi interface, and the Repository class as well as
    the remote data sources
* Domain
    * This is the domain layer and consists of the domain model as well the usecases
* Presentation
    * This is the presentation layer. I have set up packages by feature here. This consists of the view related code.
* DI
    * This is where Dagger-Hilt related code lives ,connected to the different layers of the application
* Utils
    * This is where most extension functions and constants and some other utils functions exist.

## Libraries

Libraries used:
    - Viewmodel
    - Data Binding
    - Room 
    - Paging 3
    - Retrofit
    - Coroutines
    - Hilt
    - Glide


