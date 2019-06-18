# Cake MVI

The application is structured using a clean architecture approach and consists of the following layers:

- **Data Layer**

  This layer is responsible for providing the data model objects used within the app. It contains the logic to load the data from network and the implementation details for the networking components, including Retrofit, OkHttp, and Moshi setup.

- **Domain Layer**

	This layer is responsible for connecting the presentation layer with the data layer. This contains UseCases which executed any business logic required before returning data to the presentation layer, e.g. sorting and filtering operations.

- **Presentation Layer**

	This layer is responsible for the UI implementation of the application. MVI (Model-View-Intent) architecture is used to perform view level interactions. This is achieved by using an MVI ViewModel which contains an internal state machine, this state machine accepts a series of Intents from the View, and upon processing the intents will communicate with the UseCase when necessary and update its ViewState accordingly. The ViewState is observed by the View and upon receiving a change will update the view to represent the new state. This provides unidirectional data flow whereby the ViewModel has a single entry point to accept inputs from the View, and the ViewModel has a single exit point which emits the ViewState and is observed by the View.
