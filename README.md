# Bomb-Checkers
> Variant of checkers with bomb pieces. Made for fun with help of Princeton's StdDrawPlus.

### Demo:
<img src="https://github.com/andrewdcampbell/Bomb-Checkers/blob/master/game_demo.gif" height="500"> 

## Rules
The board is an 8x8 board, with water and fire pieces on the top and bottom. Each player starts with pieces in the three rows closest to them. The front, middle-most row consists of Bomb Pieces, the second row consists of Shield Pieces, and the back, edge-most row consists of normal pieces. Only every other space on the Board is used, and all pieces can only move and capture diagonally. If a capture move is available, the player is not required to capture. The bottom left corner should have a "black" square, and should contain a normal fire piece.

Movement of pieces are the same as classic Checkers. In all new games, fire team makes the first move. Besides king pieces, fire pieces always move upwards (like flames) and water pieces always move diagonally downwards (like rain). Upon reaching the topmost row, fire pieces become "kinged" and are allowed to move not only diagonally forwards, but also diagonally backwards. The same can be said for water pieces that reach the bottommost row. Capturing a piece works exactly the same as in classic Checkers. You may perform multi-captures like in classic Checkers.

#### Normal Piece

Moves diagonally, and captures diagonally. Normal pieces can multi-capture, meaning if performing a capture lands the piece in a position ready to perform another capture, that piece may choose to do perform another capture in the same turn.

#### Shield Piece

A normal checkers piece, except that it cannot be killed by bomb explosions. However, they can still be normally captured by any piece (including bombs).

#### Bomb Piece

A normal Checkers piece with a twist. Performing a capture with a bomb piece causes an explosion in the destination landing. Explosions kill all pieces adjacent to the landing (all non-shield pieces within a 3x3 block centered at the bomb's final position), as well as the exploding bomb. If a fire bomb explodes, it will kill all fire and water non-shield pieces in range. Chain reaction explosions do not occur. If one bomb destroys another bomb via explosion, a second explosion does **not** occur. Bomb pieces cannot perform multi-captures, because they explode after the first capture

## Usage
```
java Board
```
