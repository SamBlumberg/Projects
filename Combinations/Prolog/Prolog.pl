combinations(K, N, R):-
	numlist(1, N, L),
	comhelp(K, L, R).

comhelp(0, _, []).

comhelp(K, L, [H|T]) :-
    Index is K - 1,
    getcom(H, L, Rest),
    comhelp(Index, Rest, T).

getcom(H, [H|T], T).

getcom(H, [_|T], R) :- 
    getcom(H, T, R).