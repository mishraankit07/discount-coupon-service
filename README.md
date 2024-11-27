# discount-coupon-service

How it works:
1. for every UPDATE call of the coupons (CREATE, UPDATE, DELETE), data is stored in memory in a map and updated in on cloud redis (for persistence).

2. for all GET operations and calculations, data is looked up from the map (so execution time is faster).

3. on program start, the coupons are loaded from redis.

Implemented Cases:
1. all the endpoints as specified in the document have been implemented and the service layer is having unit tests.

Unimplemented Cases:
1. ttl could have been implemented in redis to expire coupons but due to current work commitments I would not be
   able to dedicate time for this.

Limitations
1. limitations: currently the keys in redis are updated as soon as an update operation comes up (create, update. delete)
   but if for some reason redis write fails, then coupons are only there in memory and not persisted.
   This can be fixed by having a periodic job which asynchronously updates all keys in redis.

Assumption in bxGy coupon type <br/>
when we say B2G1 type coupon then only 1 quantity of item from Give array will be free

if buy array [X, Y, Z] and “get” array [A, B, C]
and in the cart we have [X, Y, A] where quantity of A is 3 with coupon type B2G1
then 1 unit of A is free for the user.
