// package Exam;

import java.util.*;;

interface TransactionData {
    static int[] arr = {} ;
    static int target = 0;

    public void countUniquePairs(int [] arr, int target);
}

class TransactionDataAnalyzer implements TransactionData {


    public void countUniquePairs(int[] arr, int target) {
        int count = 0;
        Collection<ArrayList> arrayList = new ArrayList<ArrayList>();
        for (int i = 0; i < arr.length; i++) {
            Collection<Integer> transactionData = new ArrayList<Integer>();
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[i] + arr[j] == target) {
                    transactionData.add(arr[i]);
                    transactionData.add(arr[j]);
                }
            }
            if (transactionData.size() > 0) {
                arrayList.add(new ArrayList<Integer>(transactionData));
            }
        }

        for(Collection<Integer> collection : arrayList) {
            System.out.println(collection);
        }
        return ;
    }
}

public class task1 {
    public static void main(String[] args) {
        TransactionDataAnalyzer analyzer = new TransactionDataAnalyzer();
        int[] arr = {2, 4, 3, 5, 6};
        int target = 8;
        analyzer.countUniquePairs(arr, target);
    }
}