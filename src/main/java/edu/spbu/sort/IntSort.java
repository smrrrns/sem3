package edu.spbu.sort;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by artemaliev on 07/09/15.
 */
public class IntSort {

  public static void sort (int[] array, int beg, int end)
  {
    if (array.length == 0 | beg >= end)
      return;
    int mid = beg + (end - beg) / 2;
    int flag = array[mid];

    int i = beg, j = end;
    while(i <= j)
    {
      while(array[i] < flag) i++;
      while(array[j] > flag) j--;

      if(i <= j)
      {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
        i++;
        j--;
      }
    }

    if(beg < j) sort(array, beg, j);
    if(end > i) sort(array, i, end);

  }

  public static void sort (List<Integer> list) {
    Collections.sort(list);
  }

}
