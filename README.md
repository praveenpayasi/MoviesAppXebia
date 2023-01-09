# MoviesAppXebia
# A demo android application which allow user to search and loads the movies from remote server. On click of movie list item user can navigate to detail screen. It demonstrates several aspect of Android application development. The code is fully written in Kotlin
#Application architecture
Google recommended app development architecture is followed. For this architecture ViewModel is at the core of an architecture. ViewModel is lifecycle aware and hence provides a great benefit when it comes to controlling view, data and asynchronous tasks. Check the following diagram which explains that each component in dependent on it's lower level component. e.g. Activity/Fragment is dependent on it's ViewModel. A ViewModel is connected to repository. Repository's sole responsibility it to provide data from remote server.

Why architecture component?
ViewModel is lifecycle aware and when Activity/Fragment is destroyed recreating and achieving the old state comes very easy.
Repository separates the concern and provide clear way of handling data. With little refactoring A repository can support fetching data from both local and remote data source. So caching becomes easy.
Hilt which is a new dependency injection(built on top of Dagger) framework has out of the box supports for ViewModel.
Great support for implementing pagination, background jobs, navigation, local storage and data binding.
