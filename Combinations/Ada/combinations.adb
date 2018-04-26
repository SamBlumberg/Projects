with Ada.Command_Line;
with Ada.Text_IO;
use Ada.Text_IO;

procedure combinations is
	type Combination is array (Positive range <>) of Integer;

	procedure printCombinations(N, K, Index, I: IN Integer; arr: IN Combination; data: IN OUT Combination) is
	begin
		if(Index = k) then
			for Num in 1..(k-1) loop
				Put(Integer'Image(data(Num)));
				Put(" ");
			end loop;
			Put_Line("");
			return;
		end if;

		if(I >= N) then
			return;
		end if;

		data(index) := arr(i);

		printCombinations(N, K, Index + 1, I+1, arr, data);
		printCombinations(N, K, Index, I+1, arr, data);

	end printCombinations;

	procedure getCombinations(arr: IN OUT Combination; N, K: IN Integer) is
		data: Combination(1..K);
	begin
			printCombinations(N, K, 1, 1, arr, data);
	end getCombinations;

	a1: String := Ada.Command_Line.Argument(1);
	a2: String := Ada.Command_Line.Argument(2);

	K: Integer := Integer'Value(a1) + 1;
	N: Integer := Integer'Value(a2) + 1;
	
	arr: Combination(1..N);
begin
	
	for Num in 1..N loop
		arr(Num) := Num;
	end loop;

	getCombinations(arr, N, K);

end combinations;