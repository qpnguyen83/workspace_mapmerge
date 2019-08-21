As my understanding, parallel processing is not always faster than sequential,
it depends on many conditions.
So, I provide here some ways to merge parallel with naive benchmark.
We have to wisely opt base on exact real circumstance to gain best result.

I use ConcurrentHashMap instead of Synchronized HashMap (or synchronized block)
as ConcurrentHashMap is designed to work in concurrency and of course has better performance than the latter.
