# 5200-hw4

- Completing this assignment is such a pain! I have several notes:
  1. in my "Inserter.java", a lot of the *Id numbers* I used for testing may not appear when you exeucte my code. (This is also my question
for sql id generation, for exmaple, if I delete a row, and insert a new row, the *old* row's id will disappear)
  2. minor naming conflict: so we have *SitDownRestaurants.java*, but in sql, it's *SitDownRestaurant*
  3. for "create" method in SitDownRestaurantsDao, TakeOutRestaurantsDao, and FoodCartRestaurantsDao, I think they not only should create from scratch,
but also should be able to create based on Restaurants
