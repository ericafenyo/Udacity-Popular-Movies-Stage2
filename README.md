# Popular Movies

### Created as a part of Udacity Android Developer Nanodegree Program.

# Stage 1
## Stage 1 Project Overview 
Most of us can relate to kicking back on the couch and enjoying a movie with friends and family. In this project, you’ll build an app to allow users to discover the most popular movies playing. We will split the development of this app in two stages. First, let's talk about stage 1.

In this stage you’ll build the core experience of your movies app.

Your app will:

* Present the user with a grid arrangement of movie posters upon launch.
* Allow your user to change sort order via a setting:
* The sort order can be by most popular or by highest-rated
* Allow the user to tap on a movie poster and transition to a details screen with additional information such as:

  * original title
  * movie poster image thumbnail
  * A plot synopsis (called overview in the api)
  * user rating (called vote_average in the api)
  * release date

## Prerequisite
* TMDB API Key



## Why this Project?
To become an Android developer, you must know how to bring particular mobile experiences to life. Specifically, you need to know how to build clean and compelling user interfaces (UIs), fetch data from network services, and optimize the experience for various mobile devices. You will hone these fundamental skills in this project.

By building this app, you will demonstrate your understanding of the foundational elements of programming for Android. Your app will communicate with the Internet and provide a responsive and delightful user experience.

### What Will I Learn After Stage 1?
* You will fetch data from the Internet with theMovieDB API.
* You will use adapters and custom list layouts to populate list views.
* You will incorporate libraries to simplify the amount of code you need to write

## Rubric
### Common Project Requirements

- [x] App is written solely in the Java Programming Language.

- [x] Movies are displayed in the main layout via a grid of their corresponding movie poster thumbnails.

- [x] UI contains an element (i.e a spinner or settings menu) to toggle the sort order of the movies by: most popular, highest rated.

- [x] UI contains a screen for displaying the details for a selected movie.

- [x] Movie details layout contains title, release date, movie poster, vote average, and plot synopsis.

- [x] App utilizes stable release versions of all libraries, Gradle, and Android Studio.

### User Interface - Function

- [x] When a user changes the sort criteria (“most popular and highest rated”) the main view gets updated correctly.

- [x] When a movie poster thumbnail is selected, the movie details screen is launched.

### Network API Implementation

- [x] In a background thread, app queries the /movie/popular or /movie/top_rated API for the sort criteria specified in the settings menu.

### General Project Guidelines

- [x] App conforms to common standards found in the Android Nanodegree General Project Guidelines (NOTE: For Stage 1 of the Popular Movies App, it is okay if the app does not restore the data using onSaveInstanceState/onRestoreInstanceState)

# Stage 2
## Stage 2 Project Overview
Welcome back to Popular Movies! In this second and final stage, you’ll add additional functionality to the app you built in Stage 1.

You’ll add more information to your movie details view:

* You’ll allow users to view and play trailers ( either in the youtube app or a web browser).
* You’ll allow users to read reviews of a selected movie.
* You’ll also allow users to mark a movie as a favorite in the details view by tapping a button(star).
* You'll create a database to store the names and ids of the user's favorite movies (and optionally, the rest of the information needed to display their    favorites collection while offline).
* You’ll modify the existing sorting criteria for the main view to include an additional pivot to show their favorites collection.


## What Will I Learn After Stage 2?
* You will build a fully featured application that looks and feels natural on the latest Android operating system (Nougat, as of November 2016).


## Rubric
### Common Project Requirements

- [x] App is written solely in the Java Programming Language.

- [x] App conforms to common standards found in the Android Nanodegree General Project Guidelines.

- [x] App utilizes stable release versions of all libraries, Gradle, and Android Studio.

### User Interface - Layout

- [x] UI contains an element (e.g., a spinner or settings menu) to toggle the sort order of the movies by: most popular, highest rated.

- [x] Movies are displayed in the main layout via a grid of their corresponding movie poster thumbnails.

- [x] UI contains a screen for displaying the details for a selected movie.

- [x] Movie Details layout contains title, release date, movie poster, vote average, and plot synopsis.

- [x] Movie Details layout contains a section for displaying trailer videos and user reviews.

- [x] User Interface - Function

- [x] When a user changes the sort criteria (most popular, highest rated, and favorites) the main view gets updated correctly.

- [x] When a movie poster thumbnail is selected, the movie details screen is launched.

- [x] When a trailer is selected, app uses an Intent to launch the trailer.

- [x] In the movies detail screen, a user can tap a button (for example, a star) to mark it as a Favorite. Tap the button on a favorite movie will unfavorite it.

### Network API Implementation

- [x] In a background thread, app queries the /movie/popular or /movie/top_rated API for the sort criteria specified in the settings menu.

- [x] App requests for related videos for a selected movie via the /movie/{id}/videos endpoint in a background thread and displays those details when the user selects a movie.

- [x] App requests for user reviews for a selected movie via the /movie/{id}/reviews endpoint in a background thread and displays those details when the user selects a movie.

### Data Persistence

- [x] The titles and IDs of the user’s favorite movies are stored in a native SQLite database and exposed via a ContentProvider
OR
stored using Room.

- [x] Data is updated whenever the user favorites or unfavorites a movie. No other persistence libraries are used.

- [x] When the "favorites" setting option is selected, the main view displays the entire favorites collection based on movie ids stored in the database.

### Android Architecture Components

- [x] If Room is used, database is not re-queried unnecessarily. LiveData is used to observe changes in the database and update the UI accordingly.

- [x] If Room is used, database is not re-queried unnecessarily after rotation. Cached LiveData from ViewModel is used instead.

### Suggestions to Make Your Project Stand Out!

- [x] Extend the favorites database to store the movie poster, synopsis, user rating, and release date, and display them even when offline.

- [x] Implement sharing functionality to allow the user to share the first trailer’s YouTube URL from the movie details screen.
