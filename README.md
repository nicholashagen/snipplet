# Introduction

Snipplet is an online web application for interacting with Gists via the
GitHub API in order to category, tag, and better manage Gists.  It also
allows a social aspect of following other users and public Gists and
being able to quickly snip or fork another Gist.

Gists are becoming more and more widely used as a place to store snippets
of code.  However, the management of those snippets is increasingly difficult
to deal with.  Snipplet hopes to take Gists to the next level by providing
a new web interface to add categories and tags that make searching, locating,
and re-using Gists easier.  Think of Pinterest for developers.  By using
Snipplet, you can take snippets or templates of commonly used code and
apply them to categories such as Design Patterns, Java Code, Grails code,
Python Code, Enterprise Features, Messaging Features, etc.  The categories
are custom to you to better manage your snipplets!

This code is very pre-alpha and not ready for consumption.  If you are interested
in helping bring this code to final fruition and launch, let me know.

# Getting Started

1. Fork this repository to your own repository. The Snipplet repository is read-only and operates
on the principles of pull requests.

2. Clone your personal fork of the repository onto your local development machine

3. Ensure that Grails 2.x is installed (http://grails.org)

4. Request an API Key from GitHub: https://github.com/account/applications/new

    1 Application Name should be Snipplet

    2 Main URL should be http://localhost:8080 (or whatever port your local dev server will run on)

    3 Callback URL should be http://localhost:8080/snipplet/login/github

5. Create a grails-app/conf/GithubConfig.groovy file 

    1 Enter the client id and secret in the development closure of the environment properties

        environments {
            development {
                com.znet.snipplet.gitub.clientId = "CLIENT ID"
                com.znet.snipplet.github.clientSecret = "CLIENT SECRET"
            }
        }

6. Run ``grails run-app`` to launch the application

7. Access <http://localhost:8080/snipplet/snipplets>

# Architecture

Snipplet is based on Grails for its web application and GORM for its database persistence via HSQL or MySQL. Snipplet uses the 
GitHub v3 API (<http://developer.github.com/v3/>) to retrieve and update Gists via [http://groovy.codehaus.org/HTTP+Builder HTTP Builder].  
Snipplet uses the [http://grails.org/plugin/quartz Quartz] 
plugin to schedule
jobs that periodically query the latest Gists via the API and updates its internal database with copies of those Gists as well
as automatically creating Snippets for Gists from registered users. Snipplet uses [http://twitter.github.com/bootstrap/ Bootstrap] from Twitter 
and [http://jquery.com/ jQuery] for its
user interface.  It also uses a custom version of [http://code.google.com/p/google-code-prettify/ Prettify] javascript library to handle code beautification.

## Controllers

The following controllers are the primary controllers used in the application.

### DefaultController

The DefaultController is the base controller that all other controllers should extend from generally.  It uses the Grails-based
``afterInterceptor`` closure to automatically add the list of languages to every model in order to drive the main navigation.

### LoginController

The LoginController is responsible for performing the login and registration process. It handles the OAuth login process via GitHub as
well as handling the creation of new users by automatically importing their existing Gists into Snippets.

### UserController

The UserController is responsible for handling user-specific actions such as displaying a user's snippets, snipping new Snippets, etc.

### SnippletController

The SnippletController is responsible for handling the main snipplet views to show the current Gists.

## Services

The following services are the primary services that either interact with the database via GORM or the API.

### ApiService

The ApiService is a generic API handling service that can query any API and convert the results to JSON. The API service also has a built
in monitor that tracks the `X-RateLimit-Remaining` headers.

### DateService

The DateService is a general date utility service that parses dates as JSON strings via JODA time to properly handle the format and timezone conversion.

### ErrorService

The ErrorService allows the application to store errors to the database for offline management such as failed Gist processing.

### ExecutorService

The ExecutorService is a general service for queuing tasks and concurrently processing them via the `java.util.concurrent` package.

### GistService

The GistService is responsible for querying the Gist API and converting the results to domain objects that are persisted to the database via GORM.
The service is also responsible for periodically deleting old Gists in the database to conserve space in the database.

### GithubService

The GithubService provides access to the registered client id and secret from the `GithubConfig.groovy` configuration. It also provides lookup of users
from the database that are automatically created from either registered users or from discovered Gists.

### LanguageService

The LanguageService is responsible for looking up and returning languages as provided by the Gist API. It also is responsible for counting the number of files
per language in order to drive the navigation based on popularity of languages.

### UserService

The UserService handles updating and creating the user in the database on login/logout as well as user tasks such as creating snippets.

## Page Flow

The main page is <http://localhost:8080/snipplet/snipplets> via `grails-app/views/snipplet/list`.  The URL is mapped in `UrlMappings.groovy` that also
allows the URL to include the language without using a query parameter (ie: <http://localhost:8080/snipplet/snipplets/java>).  The page retrieves the 
current Gists to display to the user.  Currently, the Gists are just the latest Gists, but at some point it may be based on metrics and scoring to present
users with more applicable Gists based on weighting via friends, languages, popularity and forks, etc.

The page is initially loaded with the gists inside a hidden div.  This allows the client side javascript to inspect the Gists, beautify the code, and layout the Gists in multiple columns based on the width of the browser viewport.

The navigation is also controlled by client-side javascript to automatically move the navigation elements to and from the navigation bar to the dropdown based on the active width of the viewport.

The login link on the navigation bar launches a modal popup that allows users to either login with GitHub or create new GitHub accounts.  The login process uses GitHub OAuth authentication by opening a window that requests user authentication.  Once the user authorizes the application, GitHub redirects back to the URL in the application configuration (ie: <http://localhost:8080/snipplet/login/github>).  This page loads javascript that closes the window and updates the main modal popup to finish the login process.  If the user is new to Snipplet, the user's current Gists will be imported into Snippets.

# Next Steps

This code is in very alpha state of development.  Once local development is completed, we hope to launch an initial site to the cloud (Cloud Foundry, Amazon, etc) for public consumption.  Our hope is that code snippets become a part of everyday development and reference with a social flare.

Come join us in development and help us launch this exciting product.
