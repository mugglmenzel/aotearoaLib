aotearoaLib
===========

A java library for programmatic decision-making and an implementation of the Analytic Hierarchy Process (AHP).

aotearoaLib provides a library to apply the Multi Criteria Comparison Method for Cloud Computing (MC^2)^2 framework. 
A model allows to define decisions that can be settled with one of multiple alternatives. 
Furthermore, every decision has goals which comprise a hierarchy of criteria. 
Every criterion is defined with a metric. 
Goals and criteria can be weighted within the model. 
An implementation of AHP processes a given model and computes a numerical evaluation of all alternatives from the model.
Evaluation results are comparable on a absolute [0, 1] scale.

Example
-------

An Example is granted by the test class UseCase.java:
https://github.com/mugglmenzel/aotearoaLib/blob/master/src/de/eorg/aotearoa/lib/tests/UseCase.java

The class gives a fair overview of the library's API and introduces the concepts of the data model.
You can run the example by checking out the code and run the class as a JUnit test.