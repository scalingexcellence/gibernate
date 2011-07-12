/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Interfaces;

/**
 *
 * @author kostas
 */
//gives to the implementing class caching functionality. Automatic purging of cache 
//is activated whenever cacheExpired() returns true,
//the condition which indicates that is left to be implemented in the implementing class
public interface Cacheable {
    void resetCacheLimit(int cacheLimit);
    void increaseNumOfOperations();
    boolean cacheExpired();
}
