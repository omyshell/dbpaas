/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lushell.tc.mypaas.executor;

import java.util.concurrent.LinkedBlockingQueue;

public class BlockQueue<E> extends LinkedBlockingQueue<E> {
  public BlockQueue(int maxSize) {
    super(maxSize);
  }

  @Override
  public boolean offer(E e) {
    // turn offer() and add() into a blocking calls (unless interrupted)
    try {
      put(e);
      return true;
    } catch (InterruptedException ie) {
      Thread.currentThread().interrupt();
    }
    return false;
  }
}