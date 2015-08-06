package com.rookiefly.session.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * redis����ӿ�
 *
 */
public interface RedisService {
    /**
     * ��redis������һ��ֵ,O(1)
     * 
     * @param key
     *            ��
     * @param value
     *            ֵ
     * @return ״̬��
     * @throws JedisConnectionException
     *             ����redis�����������Ӳ��ɹ�
     */
    String set(String key, String value);

    /**
     * ����һ��ֵ,ͬʱָ������ʱ��,O(1)
     * 
     * @param key
     *            ��
     * @param value
     *            ֵ
     * @param expireSeconds
     *            ����ʱ��(��)
     * @return ״̬��
     * @throws JedisConnectionException
     *             ����redis�����������Ӳ��ɹ�
     */
    String set(String key, String value, int expireSeconds);

    /**
     * ����һ��ֵ,ͬʱָ������ʱ��,O(1)
     * 
     * @param key
     *            �� �ֽ�����
     * @param value
     *            ֵ �ֽ�����
     * @param expireSeconds
     *            ����ʱ��(��)   
     * @return ״̬��
     * @throws JedisConnectionException
     *             ����redis�����������Ӳ��ɹ�
     */
    String set(byte[] key, byte[] value, int expireSeconds);

    /**
     * ��redis��ȡ�غ�key�������ַ���,O(1)
     * 
     * @param key   �ֽ�����
     * @return ��key��������ַ���,��key������,�򷵻�null
     * @throws JedisConnectionException
     *             ����redis�����������Ӳ��ɹ�
     */
    byte[] get(byte[] key);

    /**
     * ɾ��һ��key,O(1)
     * 
     * @param key
     *            Ҫɾ����key
     * @return ��ɾ��key����
     * @throws JedisConnectionException
     *             ����redis�����������Ӳ��ɹ�
     */
    int del(byte[] key);

    /**
     * ����һ��������Թ���ʱ��,O(1)
     * 
     * @param key
     *            ��
     * @param seconds
     *            ��������
     * @return true ���óɹ�,false ����ʧ��
     * @throws JedisConnectionException
     *             ����redis�����������Ӳ��ɹ�
     */
    boolean expire(final byte[] key, final int seconds);

    /**
     * ����һ��ֵ��set
     * 
     * @param key
     *            ��
     * @param value
     *            ֵ
     * @return ״̬��
     * @throws JedisConnectionException
     *             ����redis�����������Ӳ��ɹ�
     */
    Long addToSet(String key, String value);

    /**
     * ��set��ɾ��һ��ֵ
     * 
     * @param key
     *            ��
     * @param value
     *            ֵ
     * @return ״̬��
     * @author zhengsibi
     * 
     */
    public Long removeFormSet(String key, String value);

    /**
     * ����һ��ֵ
     * 
     * @param key
     * @param value
     * @return
     */
    public Long addToSet(String key, String... value);

    /**
     * ��ȡһ��set��Ԫ����
     * 
     * @param key
     * @return int (number of set)
     * @throws JedisConnectionException
     *             ����redis�����������Ӳ��ɹ�
     */
    int getSetCount(String key);

    /**
     * ����һ��keyֵ��ȡһ��set���ϵ�Ԫ��,O(N)
     * 
     * @param key
     * @return
     * @throws JedisConnectionException
     *             ����redis�����������Ӳ��ɹ�
     * 
     */
    Set<String> getSetMembers(String key);

    /**
     * �������set�е�һ��Ԫ��,O(1)
     * 
     * @param key
     *            ��
     * @return �����set��ѡȡ��һ��Ԫ��
     * 
     * @throws JedisConnectionException
     *             ����redis�����������Ӳ��ɹ�
     */
    String getRandomMember(String key);

    /**
     * ��һ����������,�򽫼����óɶ�Ӧ��ֵ,O(1)
     * 
     * @param key
     *            ��
     * @param value
     *            ֵ
     * @return true ���óɹ� false ���Ѵ���
     * @throws JedisConnectionException
     *             ����redis�����������Ӳ��ɹ�
     */
    boolean setIfNotExist(String key, String value);

    /**
     * ��һ����������,�򽫼����óɶ�Ӧ��ֵ�͹���ʱ��
     * 
     * @param key
     *            ��
     * @param value
     *            ֵ
     * @param expireMilliSeconds
     *            ����ʱ�����
     * @return true ���óɹ� false ���Ѵ���
     * @throws JedisConnectionException
     *             ����redis�����������Ӳ��ɹ�
     */
    boolean setIfAbsent(String key, String value, int expireMilliSeconds);

    /**
     * ��redis��ȡ�غ�key�������ַ���,O(1)
     * 
     * @param key
     * @return ��key��������ַ���,��key������,�򷵻�null
     * @throws JedisConnectionException
     *             ����redis�����������Ӳ��ɹ�
     */
    String get(String key);

    /**
     * ��ȡһ��ֵ,O(n),nΪ���ĸ���
     * 
     * @param keys
     *            һ����
     * @return ��Ӧ��һ��ֵ,��ĳ����������,���Ӧ��ֵΪnull
     * @throws JedisConnectionException
     *             ����redis�����������Ӳ��ɹ�
     */
    List<String> gets(String... keys);

    /**
     * �ж�key�Ƿ����,O(1)
     * 
     * @param key
     * @return true ����,false ������
     * @throws JedisConnectionException
     *             ����redis�����������Ӳ��ɹ�
     */
    boolean exists(String key);

    /**
     * ��ȡָ��Key��ָ����Ա�ķ�����O(1)
     * 
     * @param key
     * @param value
     * @return
     */
    Double getSetScore(String key, String value);

    /**
     * sortedset key�д洢���ֶ�value �����ֵ�ǵ�ǰʱ���
     * 
     */
    void addSortedSet(String key, String value);

    /**
     * ɾ��һ��key,O(1)
     * 
     * @param keys
     *            Ҫɾ����key
     * @return ��ɾ��key����
     * @throws JedisConnectionException
     *             ����redis�����������Ӳ��ɹ�
     */
    int del(String... keys);

    /**
     * ����һ��������Թ���ʱ��,O(1)
     * 
     * @param key
     *            ��
     * @param seconds
     *            ��������
     * @return true ���óɹ�,false ����ʧ��
     * @throws JedisConnectionException
     *             ����redis�����������Ӳ��ɹ�
     */
    boolean expire(String key, int seconds);

    /**
     * ����һ�����ľ��Թ���ʱ��,O(1)
     * 
     * @param key
     *            ��
     * @param unixTime
     *            unixʱ���(��)
     * @return �Ƿ�ɹ�
     * @throws JedisConnectionException
     *             ����redis�����������Ӳ��ɹ�
     */
    boolean expireAt(String key, long unixTime);

    /**
     * ԭ���Եؽ�һ�������ó���ֵ,ͬʱ���ؾ�ֵ,O(1)
     * 
     * @param key
     *            ��
     * @param value
     *            Ҫ���õ�ֵ
     * @return ��ֵ
     * @throws JedisConnectionException
     *             ����redis�����������Ӳ��ɹ�
     */
    String getSet(String key, String value);

    /**
     * ��һ������ֵ(ԭ���Ե�)��1
     * 
     * @param key
     *            ��
     * @return ���Ӻ��ֵ
     * @throws JedisConnectionException
     *             ����redis�����������Ӳ��ɹ�
     */
    long incr(String key);

    /**
     * ��һ������ֵ(ԭ���Ե�)����n
     * 
     * @param key
     *            ��
     * @param increment
     *            ����
     * @return ���Ӻ��ֵ
     * @throws JedisConnectionException
     *             ����redis�����������Ӳ��ɹ�
     */
    long incrBy(String key, int increment);

    /**
     * ��һ������ֵ(ԭ���Ե�)����n
     * 
     * @param key
     *            ��
     * @param increment
     *            ����
     * @return ���Ӻ��ֵ
     * @throws JedisConnectionException
     *             ����redis�����������Ӳ��ɹ�
     */
    Double incrByFloat(String key, double increment);

    /**
     * ��һ������ֵ(ԭ���Ե�)��1
     * 
     * @param key
     *            ��
     * @return ���ٺ��ֵ
     * @throws JedisConnectionException
     *             ����redis�����������Ӳ��ɹ�
     */
    long decr(String key);

    /**
     * ��һ������ֵ(ԭ���Ե�)��n
     * 
     * @param key
     *            ��
     * @param decrement
     *            ���ٵ���
     * @return ���ٺ��ֵ
     * @throws JedisConnectionException
     *             ����redis�����������Ӳ��ɹ�
     */
    long decrBy(String key, int decrement);

    /**
     * ��hashtable������һ��ֵ,O(1)
     * 
     * @param key
     *            hashtable��key
     * @param field
     *            hashtable��field
     * @param value
     *            Ҫ���õ�ֵ
     * @return 0��������ԭ����ֵ,1:�½���һ��ֵ
     * @throws JedisConnectionException
     *             ����redis�����������Ӳ��ɹ�
     */
    int hset(String key, String field, String value);

    /**
     * ��hashtable������һ����ֵ��,O(N),NΪ��ֵ�Ը���
     * 
     * @param key
     *            hashtable�ļ�
     * @param fields
     *            Ҫ���õ�һ����ֵ��
     * @throws JedisConnectionException
     *             ����redis�����������Ӳ��ɹ�
     */
    void hsets(String key, Map<String, String> fields);

    /**
     * ��һ��hashtalbe��filed������,��������Ϊһ��ֵ ,��������
     * 
     * @param key
     *            hashtalbe��key
     * @param field
     *            hashtable�� field
     * @param value
     *            Ҫ���õ�ֵ
     * @return true ���óɹ�,false field�Ѵ���
     * @throws JedisConnectionException
     *             ����redis�����������Ӳ��ɹ�
     */
    boolean hsetIfNotExists(String key, String field, String value);

    /**
     * ��ȡhashtable��һ��field��Ӧ��ֵ
     * 
     * @param key
     *            hashtable�ļ�
     * @param field
     *            hashtable��field
     * @return field��Ӧ��ֵ
     * @throws JedisConnectionException
     *             ����redis�����������Ӳ��ɹ�
     */
    String hget(String key, String field);

    /**
     * ��ȡһ��hasht
     * 
     * @param key
     * @param fields
     * @return fields��Ӧ��ֵ
     */
    List<String> hgets(String key, String... fields);

    /**
     * ��ȡhashtable�����еļ�ֵ��
     * 
     * @param key
     *            hashtable��key
     * @return hashtable�����м�ֵ��,����һ��key������hashtable������,�򷵻�empty��map
     */
    Map<String, String> hgetAll(String key);

    /**
     * ɾ��ָ��key���ڵ�hashtable��ĳ��field
     * 
     * @param key
     *            ָ��hashtable��key
     * @param field
     *            hashtable��field
     * @author zhengsibi
     */
    void hdel(String key, String field);

    /**
     * ɾ��ָ��key���ڵ�hashtable�ж��field
     * 
     * @param key
     * @param fields
     */
    void hdel(String key, String... fields);

    /**
     * ��һ��ͨ������һ����Ϣ,���ĸ�ͨ���Ŀͻ��˽����ܵ�������Ϣ
     * 
     * @param channel
     *            ͨ��
     * @param message
     *            Ҫ���͵���Ϣ
     * @throws JedisConnectionException
     *             ����redis�����������Ӳ��ɹ�
     */
    void publish(String channel, String message);

    /**
     * ����redis�д洢������ one of "none","set","list","string" "hash" key is not
     * exsits retrun none
     * 
     * @param key
     * @return
     */
    String type(String key);

    /**
     * Return all the fields in a hash.
     * 
     * @param key
     * @return
     */
    Set<String> hkeys(String key);

    /**
     * Set the respective fields to the respective values. HMSET replaces old
     * values with new values.
     * 
     * @param key
     * @param hash
     * @return
     */
    String hmset(String key, Map<String, String> hash);

    /**
     * <pre>
     * ����minuend��key��ԭֵ�Ĳ�(��keyδ�������κ�ֵ,����Ϊkey��ԭֵΪ0),����key��ԭֵ����Ϊminuend
     * �˷����������ڷֲ�ʽ�����¼�������������(�����ʱ)
     * </pre>
     * 
     * @param minuend
     *            ������
     * @param key
     *            ������Ӧ��key
     * @return minuend��key��ԭֵ�Ĳ�
     */
    long substractedAndSet(long minuend, String key);

    /**
     * ����ȡttlʱ�䷽��
     * 
     * @param key
     *            ��Ӧ��key
     * @return ����ʧЧ������
     */
    public long ttl(String key);

    String toString();

    public Long zcount(String key, double min, double max);

    long zadd(String key, String member, double score);

    long zadd(String key, Map<Double, String> members);

    Set<String> zrange(String key, long start, long end);

    Set<String> zrangeAndDel(String key, long start, long end);

    long zrem(String key, String... member);

    long zRemrangeByScore(String key, double min, double max);

    Set<String> zreverage(String key, long start, long end);

    /**
     * Increment the number stored at field in the hash at key by value. If key
     * does not exist, a new key holding a hash is created. If field does not
     * exist or holds a string, the value is set to 0 before applying the
     * operation. Since the value argument is signed you can use this command to
     * perform both increments and decrements.
     * 
     * @param key
     * @param member
     * @return
     */
    public long hincrBy(String key, String member);

    /**
     * Increment the number stored at field in the hash at key by value. If key
     * does not exist, a new key holding a hash is created. If field does not
     * exist or holds a string, the value is set to 0 before applying the
     * operation. Since the value argument is signed you can use this command to
     * perform both increments and decrements.
     * 
     * @param key
     * @param member
     * @param dlt
     * @return
     */
    long hincrBy(String key, String member, int dlt);

    /**
     * Return the sorted set cardinality (number of elements). If the key does
     * not exist 0 is returned, like for empty sorted sets.
     * 
     * Time complexity O(1)
     * 
     * @param key
     * @return
     */
    public long zcard(String key);

    /**
     * Remove the specified member from the sorted set value stored at key. If
     * member was not a member of the set no operation is performed. If key does
     * not not hold a set value an error is returned. Time complexity O(log(N))
     * with N being the number of elements in the sorted set
     * 
     * @param key
     * @param members
     */
    public long removeSortedSet(String key, String... members);

    /**
     * Return the length of the list stored at the specified key. If the key
     * does not exist zero is returned (the same behaviour as for empty lists).
     * If the value stored at key is not a list an error is returned.
     * <p>
     * Time complexity: O(1)
     * 
     * @param key
     * @return The length of the list.
     */
    public long llen(String key);

    /**
     * Return the specified elements of the list stored at the specified key.
     * Start and end are zero-based indexes. 0 is the first element of the list
     * (the list head), 1 the next element and so on.
     * <p>
     * For example LRANGE foobar 0 2 will return the first three elements of the
     * list.
     * <p>
     * start and end can also be negative numbers indicating offsets from the
     * end of the list. For example -1 is the last element of the list, -2 the
     * penultimate element and so on.
     * <p>
     * <b>Consistency with range functions in various programming languages</b>
     * <p>
     * Note that if you have a list of numbers from 0 to 100, LRANGE 0 10 will
     * return 11 elements, that is, rightmost item is included. This may or may
     * not be consistent with behavior of range-related functions in your
     * programming language of choice (think Ruby's Range.new, Array#slice or
     * Python's range() function).
     * <p>
     * LRANGE behavior is consistent with one of Tcl.
     * <p>
     * <b>Out-of-range indexes</b>
     * <p>
     * Indexes out of range will not produce an error: if start is over the end
     * of the list, or start > end, an empty list is returned. If end is over
     * the end of the list Redis will threat it just like the last element of
     * the list.
     * <p>
     * Time complexity: O(start+n) (with n being the length of the range and
     * start being the start offset)
     * 
     * @param key
     * @param start
     * @param end
     * @return Multi bulk reply, specifically a list of elements in the
     *         specified range.
     */
    public List<String> lrange(String key, final long start, final long end);

    /**
     * Atomically return and remove the last (tail) element of the srckey list,
     * and push the element as the first (head) element of the dstkey list. For
     * example if the source list contains the elements "a","b","c" and the
     * destination list contains the elements "foo","bar" after an RPOPLPUSH
     * command the content of the two lists will be "a","b" and "c","foo","bar".
     * <p>
     * If the key does not exist or the list is already empty the special value
     * 'nil' is returned. If the srckey and dstkey are the same the operation is
     * equivalent to removing the last element from the list and pusing it as
     * first element of the list, so it's a "list rotation" command.
     * <p>
     * Time complexity: O(1)
     * 
     * @param srckey
     * @param dstkey
     * @return Bulk reply
     */
    public String rpop(final String key);

    /**
     * Atomically return and remove the first (LPOP) or last (RPOP) element of
     * the list. For example if the list contains the elements "a","b","c" LPOP
     * will return "a" and the list will become "b","c".
     * <p>
     * If the key does not exist or the list is already empty the special value
     * 'nil' is returned.
     * 
     * @see #rpop(String)
     * 
     * @param key
     * @return Bulk reply
     */
    public String lpop(final String key);

    /**
     * Add the string value to the head (LPUSH) or tail (RPUSH) of the list
     * stored at key. If the key does not exist an empty list is created just
     * before the append operation. If the key exists but is not a List an error
     * is returned.
     * <p>
     * Time complexity: O(1)
     * 
     * @param key
     * @param strings
     * @return Integer reply, specifically, the number of elements inside the
     *         list after the push operation.
     */
    public Long rpush(final String key, final String string);

    /**
     * Add the string value to the head (LPUSH) or tail (RPUSH) of the list
     * stored at key. If the key does not exist an empty list is created just
     * before the append operation. If the key exists but is not a List an error
     * is returned.
     * <p>
     * Time complexity: O(1)
     * 
     * @param key
     * @param strings
     * @return Integer reply, specifically, the number of elements inside the
     *         list after the push operation.
     */
    public Long lpush(final String key, final String string);

    /**
     * if redis ${key} exists and value in ${set} return true else false
     * 
     * add by david.ling 2014-08-27
     * 
     * @param key
     * @param value
     * @return
     */
    public boolean isMember(String key, String value);

    /**
     * set add member
     * 
     * add by david.ling 2014-08-27
     * 
     * @param key
     * @param value
     * @return
     */
    public Long addSet(String key, String value);

    /**
     * 
     * @param channel
     * @param message
     */
    void originalPublish(String channel, String message);

    /**
     * ����ͨ�����ȡ����key ʱ�临�Ӷ�: O(N), N Ϊ���ݿ��� key ��������
     * 
     * @param pattern
     *            keyͨ���
     * @return
     */
    Set<String> keys(String pattern);

    /**
     * ����lpush
     * 
     * @param key
     * @param valueList
     * @return
     */
    public int batchLpush(String key, List<String> valueList);

    /**
     * ����zrangeByScore
     * 
     * @param keys
     * @param min
     * @param max
     * @param offset
     * @param count
     * @return
     */
    public Map<String, Set<String>> batchZRevrangeByScore(Set<String> keys, double max, double min, int offset,
                                                          int count);

    /**
     * ����zrangeByScore
     * 
     * @param keys
     * @param min
     * @param max
     * @param offset
     * @param count
     * @return
     */
    public Map<String, Set<String>> batchZRrangeByScore(Set<String> keys, double min, double max, int offset, int count);

    public Long rpush(final byte[] key, final byte[]... strings);

    public byte[] lindex(final byte[] key, final long index);

}
