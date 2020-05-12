# memory-match

A simple game written in Java using Android Studio.

Fetches images from URLs embedded in JSON from a JSON endpoint, randomizes images onto buttons to form corresponding pairs. Find all the pairs by flipping over 2 cards at a time and you win!

![Game with no matches made](/images/no-matches.png)

![Game with some matches made](/images/some-matches.png)

![Game when won](/images/win.png)

### Known issues
* JSON endpoint contains some images with different URLs but they are actually the same image. This can cause weird behaviour for the player if two sets of the same image from different URLs get randomly chosen. Images that look like they match would be rejected by the game when they are indeed the same image, just from different URLs. This is a limitation of the provided JSON endpoint but could be fixed with image comparison such that duplicate images do not get saved from the URLs assuming the images are identical pixel-for-pixel.

### Possible improvements and new features
* Dynamic button population for variable grid sizes. Currently using a fixed layout with 20 buttons for simplicity.
* Continuable game - once game is over, you can continue and gain more score instead of starting over. This would also require a score system as well as a high score tracker and possible saved game state.
* Configurable # of cards to match (eg. 3 cards must match instead of 2).
* Shuffle button for added challenge.
