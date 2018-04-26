
let makeArray a = 
	let arr = Array.make a 0 in
		for i = 0 to a-1 do
			arr.(i) <- (i+1);
		done;
	print_string "\n";
    Array.to_list arr;
;;

let rec printList inp k =

	if inp = [] then
		print_string "\n"
	else begin
		let se = Array.of_list (List.hd (inp)) in
			for i = 0 to k-1 do
				print_int (se.(i));
				print_string " ";
			done;
		print_string "\n";
		printList (List.tl inp) k;
	end
;;

	
let rec combine k list =
    if k <= 0 then [ [] ]
    else match list with
         | [] -> []
         | h :: tl ->
            let with_h = List.map (fun l -> h :: l) (combine (k-1) tl) in
            let without_h = combine k tl in
            with_h @ without_h
;;

let k = int_of_string (Sys.argv.(1));;
let n = int_of_string (Sys.argv.(2));;
printList (combine k (makeArray n)) k;;

