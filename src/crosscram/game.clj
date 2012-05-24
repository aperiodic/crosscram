(ns crosscram.game
  "Game knowledge.

In this documentation, \"vector\" means any indexed, sequential collection.

A board is a 2-dimensional matrix. Create with #'board, access with
#'location-empty?

A cell on the board is addressed by a vector of [row column], zero-indexed.
These may also be called coordinates or squares.

A domino is a vector of two squares: [[r0 c0] [r1 c1]].
The order of the squares in a vector is not important, but the game engine
will not alter it.

A game history is a vector of dominoes in order of play. The index of each
domino in the vector is the ordinal of the move that placed it.

The board contains an alternate view of history. Each cell contains either
the ordinal (from the history vector) of the move that covered that square,
or nil if the square is open.

A gamestate value (which is provided to the bot) is a map of:
:board - a board value, as defined above
:dims - a vector of [row-count column-count]
:history - a history value, as defined above

This will sometimes simply be called a game value.")

;; Implementation details

;; In 2 dimensions:
;; - A board is a 2-level nesting of vectors. The top-level vector contains
;;   the row vectors.
;; - The order of the squares in a domino is not currently important to
;;   the game engine, but the history will contain the same ordering
;;   the bots provide.

;; TODO(timmc:2012-05-23) Should we canonicalize the order of the squares in
;; the domino on receipt from a bot?

(defn board
  "Given a dimensions vector of [rows, columns], generate an empty board."
  [[rows columns]]
  (vec (repeat rows (vec (repeat columns nil))))) ;; TODO dimension-agnostic

(defn domino-squares
  "Return a sequence of the coordinates occupied by a domino."
  [domino]
  (seq domino))

(defn ^:internal set-square
  "Set the value of a square in a board."
  [board square val]
  (assoc-in board square val))

(defn place-domino
  "Place a domino on the board, assumed to be a valid move. The returned
board will have the specified move ordinal in the squares covered by the
domino."
  [board domino move-ord]
  (reduce #(set-square % %2 move-ord) board (domino-squares domino)))

(defn lookup-square
  "Discover if a board position is empty. Given a location [r c] on a board,
return the ordinal of the move that filled it, or nil if empty."
  [board square]
  (get-in board square nil))

