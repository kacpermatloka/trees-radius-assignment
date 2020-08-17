# Tree Radius Interview Assignment

Hi there! Congratulations on making it to the next step!

You are given a scaffold application based on Spring Boot to save your time, but you are free to use any other frameworks if you would like to.

Your task is to implement a specific feature as a REST service that uses some 3rd party API.
A service should make an aggregated search of trees (plants, not binary trees) in the circle.

Input:
  - X and Y of the cartesian center point
  - A search radius in meters

Output:
  - Aggregation by "common name" (please see in the documentation on the same page above which field to refer to)

Example of the expected output:
```json
{
    "red maple": 30,
    "American linden": 1,
    "London planetree": 3
}
```

The service should use the data from the 3rd party API (https://data.cityofnewyork.us/Environment/2015-Street-Tree-Census-Tree-Data/uvpi-gqnh): `https://data.cityofnewyork.us/resource/nwxe-4ae8.json`

If you happen to have any questions, please send an email to your HR contact at Holidu.

Good luck and happy coding!


#Solution
Run App.java as Java Application

Application is running on http://localhost:8080. You can use the following sample request:
```
http://localhost:8080/trees?x=988000&y=200100&radius=300
```

Performance optimizations:
- reduced searching area (SearchFrame class)
- concurrent execution for circles with a radius > 1000 meters
- JSON Streaming API to process responses without loading them all into memory
- small optimization in check whether point is within circle

Note:
In order to get more precise data please increase searchLimit value which is set to 1000 (default by 3rd party API).