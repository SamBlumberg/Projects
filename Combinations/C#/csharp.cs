using System;
namespace csharp
{
	class Hello
	{
		static int Main(string[] args)
		{
			int k = Convert.ToInt32(args[0]);

			int n = Convert.ToInt32(args[1]);

			int[] data = new int[k];

			combinations(data, 0, n - 1, 0, k);

			return 0;
		}

		static void combinations(int[] data, int start, int end, int index, int k)
		{

			if (index == k)
			{
				for (int j = 0; j < k; j++)
					Console.Write(data[j] + " ");
				Console.WriteLine("");
				return;
			}

			for (int i = start; i <= end; i++)
			{
				data[index] = i + 1;
				combinations(data, i + 1, end, index + 1, k);
			}
			

		}
	}
}


