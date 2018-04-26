import sys

def main():
    if len(sys.argv) == 3:
        k = int(sys.argv[1])
        n = int(sys.argv[2])
        arr = []
        for x in range(0, n):
            arr.append(x + 1)
        helper(arr, k, n)
    else:
        print("Incorrect Input")

def combination(arr, k, n, index, data, i):
    if index == k:
        for x in range (0, k):
            print(data[x], end=' ')
        print()
        return
    if i >= n:
        return

    data[index] = arr[i]
    combination(arr, k, n, index+1, data, i+1)
    combination(arr, k, n, index, data, i+1)


def helper(arr, k, n):
    data = [0] * n
    combination(arr, k, n, 0, data, 0)


main()


