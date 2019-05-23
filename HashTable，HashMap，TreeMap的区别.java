Map接口:保存一元偶对象的最大父接口

Map集合

TreeSet 代码里实际默认是利用 TreeMap 实现的，Java 类库创建了一个 Dummy 对象“PRESENT”作为 value，然后所有插入的元素其实是以键的形式放入了 TreeMap 里面；同理，HashSet 其实也是以 HashMap 为基础实现的，原来他们只是 Map 类的马甲！

Map接口是java保存二元偶对象（键值对）的最顶层接口。key值唯一，通过一个Key值可以找到唯一一个value值。

public  interface  Map<k，v>
 

Map中的核心方法

public  V  put  ( k  key,  V  value )：向Map中添加数据
 
public   V  get（ K key）：根据指定的key值取得相应的value值，若没有测value值，返回null。
 
public  Set<Map.Entry<K,V>> entrySet()：将Map集合变为Set集合
 
public Set<K>  KeySet()：返回所有key值集合，key不能重复
 
public Collection<V>  values()：返回所有value值，value可以重复。
Map接口有如下四个常用子类：

HashMap（使用频率最高--必考），TreeMap，Hashtable，ConcurrentHashMap

 

Hashtable、HashMap、TreeMap 都是最常见的一些 Map 实现，是以键值对的形式存储和操作数据的容器类型。

Hashtable 是早期 Java 类库提供的一个哈希表实现，本身是同步的，不支持 null 键和值，由于同步导致的性能开销，所以已经很少被推荐使用。
HashMap 是应用更加广泛的哈希表实现，行为上大致上与 HashTable 一致，主要区别在于HashMap 不是同步的，支持 null 键和值等。通常情况下，HashMap 进行 put 或者 get 操作，可以达到常数时间的性能，所以它是绝大部分利用键值对存取场景的首选，比如，实现一个用户 ID 和用户信息对应的运行时存储结构。在HashMap下有一个子类:LinkedHashMap,有序Map，序指的是元素的添加顺序
TreeMap 则是基于红黑树的一种提供顺序访问的 Map，和 HashMap 不同，它的 get、put、remove 之类操作都是 O（log(n)）的时间复杂度，具体顺序可以由指定的 Comparator 来决定，或者根据键的自然顺序来。TreeMap有序Map,序指的是Comparator或Compareable
 
Map->Set
Set接口与Map接口的关系:

 
public  Set<Map.Entry<K,V>> entrySet()：将Map集合变为Set集合
Set接口穿了马甲的Map接口,本质上Set接口的子类都是使用Map来存储元素的，都是将元素存储到Map的key值而已，value都是用共同的一个空Object对象。

 

HashTable（k-v不允许为null）
JDK1.0提供有三大主要类：Vector、Enumeration、Hashtable。Hashtable是最早实现这种二元偶对象数据结构，后期
的设计也让其与Vector一样多实现了Map接口而已。

Hashtable:单纯的哈希表实现

1.线程安全的实现:
在put、get、remove等方法上使用方法级的内建锁,锁的是当前Hashtable对象，即整个哈希表
因为内建锁所得当前table对象，所以当访问一个桶时就会把真个哈希表锁住，效力比较低
2.如何优化此性能问题?
1）JDK8之前ConcurrentHashMap思路:

通过锁细粒度化，将整表锁拆分为多个锁进行优化。

实现思路:  JDK7的ConcurrentHashMap:哈希表

将原先的16个桶设计改为16个Segment,每个Segment都有独立的一把锁。拆分后的每个Segment都相当于原先的一个HashMap(double-hash设计).并且Segment在初始化后无法扩容，每个Segment对应的哈希表可以扩容,扩容只扩容相应Segment下面的哈希表。Segment之间相互不影响

先判断我在哪个segment下面，然后再hash一次判断我在哪个segment的哪个具体的桶里面，然后进行链表存储。JDK1.7concurrentHashMap底层是两个哈希表的嵌套

线程安全:使用ReentrantLock保证相应Segment下的线程安全

2.JDK8下的ConcurrentHashMap:

整体结构与HashMap别无二致，都是使用哈希表+红黑树结构

线程安全:使用内建锁Sychronized+CAS锁每个桶的头结点,使得锁进一步细粒度化

ConcurrentHashMap不允许键值对为空

 

JDK7与JDK8 ConcurrentHashMap的变化:
1.结构上的变化:

取消原先的Segment设计，取而代之的是使用与HashMap同样的数据结构，

即哈希表+红黑树,并且引入了懒加载机制。（JDK1.7一上来就初始化，JDK1.8 在第一次put时才初始化）

2.线程安全:

1）锁粒度更细:由原来的锁Segment一片区域到锁桶的头结点

2）由原先的ReentrantLock替换为Sychronized+CAS:

现版本的sychronized已经经过不断优化，性能上与ReentrantLock基本没有差异，

并且相对于ReentrantLock，使用Sychronized可以节省大量内存空间（原来ReentrantLock下的segment都得加入同步队列，都得继承AQS下的Node，而synchronized只是锁住头结点，头结点下边的节点都不会加入同步队列里，所以 节省了空间），这是非常大的优势所在。

 


HashMap
HashMap也是一个快速失败的策略

HashMap源码解析:
1.成员变量:树化、数据结构

           ··1	DEFAULT_INITIAL_CAPACITY(初始化容量-桶数量) : 16
             2	DEFAULT_LOAD_FACTOR(负载因子): 0.75
             3	TREEIFY_THRESHOLD(树化阈值) : 8（默认是8，可以改）
             4	MIN_TREEIFY_CAPACITY(树化最少元素个数) : 64
             5	UNTREEIFY_THRESHOLD(解树化，返回链表的阈值) : 6
             6	Node<K,V>[] table : 真正存储元素的哈希表
 

 HashMap 内部的结构
它可以看作是数组（Node[] table）和链表结合组成的复合结构，数组被分为一个个桶（bucket），通过哈希值决定了键值对在这个数组的寻址；哈希值相同的键值对，则以链表形式存储，你可以参考下面的示意图。这里需要注意的是，如果链表大小超过阈值（TREEIFY_THRESHOLD, 8），图中的链表就会被改造为树形结构



 

为什么要树化：
因为之前链表如果太长查找速度很慢，变成红黑树速度快很多（log（n））。
本质上这是个安全问题。因为在元素放置过程中，如果一个对象哈希冲突，都被放置到同一个桶里，则会形成一个链表，我们知道链表查询是线性的，会严重影响存取的性能。
而在现实世界，构造哈希冲突的数据并不是非常复杂的事情，恶意代码就可以利用这些数据大量与服务器端交互，导致服务器端 CPU 大量占用，这就构成了哈希碰撞拒绝服务攻击
 

树化逻辑:
当一个桶中链表元素个数>=8,并且哈希表中所有元素个数加起来超过64，此时会将此桶中链表结构转为红黑树结构(提高链表过长导致的查找太慢问题，从原来的O(n)优化为O(logn),减少哈希碰撞（目的是解决安全问题）)
注意：

1）若只是链表个数大于8而哈希表元素不超过64，此时只是简单的resize而已，并不会树化。

         树化改造，对应逻辑主要在 putVal 和 treeifyBin 中

可以理解为，当 bin 的数量大于 TREEIFY_THRESHOLD 时：
如果容量小于 MIN_TREEIFY_CAPACITY，只会进行简单的扩容。
如果容量大于 MIN_TREEIFY_CAPACITY ，则会进行树化改造。
2）因为早期黑客利用哈希碰撞攻击，让服务器的CPU飙到一百，哈希碰撞拒绝服务攻击。

3）哈希的时间复杂度为O(1)，在二叉树里面查找一个元素的时间复杂度为log(n)

 

HashMap的负载因子
HashMap同样采用懒加载策略(不会在对象产生时初始化哈希表)

无参构造:
 
// 初始化负载因子,默认桶数目16，负载因子0.75
 
this.loadFactor = DEFAULT_LOAD_FACTOR;
为什么需要在乎容量和负载因子呢？

这是因为容量和负载系数决定了可用的桶的数量，空桶太多会浪费空间，如果使用的太满则会严重影响操作的性能。极端情况下，假设只有一个桶，那么它就退化成了链表，完全不能提供常数时间存的性能。
应该如何选择负载因子：如果能够知道 HashMap 要存取的键值对数量，可以考虑预先设置合适的容量大小。具体数值我们可以根据扩容发生的条件来做简单预估，根据前面的代码分析，我们知道它需要符合计算条件：负载因子 * 容量 > 元素
对使用负载因子的建议：

如果没有特别需求，不要轻易进行更改，因为 JDK 自身的默认负载因子是非常符合通用场景的需求的。
如果确实需要调整，建议不要设置超过 0.75 的数值，因为会显著增加冲突，降低 HashMap的性能。
如果使用太小的负载因子，按照上面的公式，预设容量值也进行调整，否则可能会导致更加频繁的扩容，增加无谓的开销，本身访问性能也会受影响。
HashTable和HashMap的区别
NO	        区别	               Hashtable	            HashMap ----- 类比HashSet
        1	       版本	              JDK1.0	            JDK1.2产生
        2	       性能	             同步处理。性能较低	异步处理，性能高
        3	      安全性	使用方法上加锁，效率低，线程安全	异步处理，效率高，线程不安全
        4	       null操作	  key和value均不允许为null	
允许key和value为null，且key值只有一个为null，value可以有多个为null。

        5	     底层实现	           底层哈希表	底层哈希表+红黑树（JDK8）

虽然 LinkedHashMap 和 TreeMap 都可 以保证某种顺序,但二者还是非常不同的. 

1）LinkedHashMap 通常提供的是遍历顺序符合插入顺序,它的实现是通过为条目(键值对) 维护一个双向链表.注意,通过特定构造函数,我们可以创建反映访问顺序的实例,所谓的put、get、compute 等，都算作“访问”。

LinkedHashMap适用场景：一些特定应用场景，例如，我们构建一个空间占用敏感的资源池，希望可以自动将最不常被访问的对象释放掉，这就可以利用 LinkedHashMap 提供的机制来实现，

2） TreeMap，它的整体顺序是由键的顺序关系决定的，通过 Comparator 或Comparable（自然顺序）来决定。

HashMap的get()源码:
1)当表还未初始化或者key值为null,return null

2)表已经初始化并且key不为null

    I.key值刚好是桶的头结点，直接返回

    II.遍历桶的其他节点

        a.若已经树化，调用树的遍历方式找到指定key对应的Node返回

        b.调用链表的遍历方式找到指定key对应的Node返回

 public V get(Object key) {
        Node<K,V> e;
        return (e = getNode(hash(key), key)) == null ? null : e.value;
    }
 
 
final Node<K,V> getNode(int hash, Object key) {
        Node<K,V>[] tab; Node<K,V> first, e; int n; K k;
        if ((tab = table) != null && (n = tab.length) > 0 &&
            (first = tab[(n - 1) & hash]) != null) {
            if (first.hash == hash && // always check first node
                ((k = first.key) == key || (key != null && key.equals(k))))
                // 当前节点key刚好是桶的第一个节点
                return first;
            // 遍历桶的其他节点找到指定key返回其value    
            if ((e = first.next) != null) {
                // 树的遍历
                if (first instanceof TreeNode)
                    return ((TreeNode<K,V>)first).getTreeNode(hash, key);
                // 链表的遍历
                do {
                    if (e.hash == hash &&
                        ((k = e.key) == key || (key != null && key.equals(k))))
                        return e;
                } while ((e = e.next) != null);
            }
        }
        // 当表还未初始化或者key值是null
        return null;
    }
 

HashMap的put源码分析
1）.若HashMap还未初始化，先进行哈希表的初始化操作(默认初始化为16个桶)，resize 方法会负责初始化它，这从 tab = resize() 可以看出。

2）.对传入的key值做hash，得出要存放该元素的桶编号

        a.若没有发生碰撞,即头结点为空,将该节点直接存放到桶中作为头结点

        b.若发生碰撞

              1.此桶中的链表已经树化，将节点构造为树节点后加入红黑树

              2.链表还未树化，将节点作为链表的最后一个节点入链表

3）.若哈希表中存在key值相同的元素，替换最新的value值

4）.在放置新的键值对的过程中，如果发生下面条件，就会发生扩容，调用resize()扩容哈希表。

thresholed = 容量(默认16) * 负载因子 (默认0.75)

if (++size > threshold)
       resize();
   5）thresholed = 容量(默认16) * 负载因子 (默认0.75)

i = (n - 1) & hash
 HashMap中的hash方法:

static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}
问题：

1.为何不采用Object类提供的hashCode方法计算出来的key值作为桶下标:

基本不会发生碰撞，哈希表就和普通数组基本没有区别

2.为何h >>> 16?（为何取出key值得高16位右移参与hash运算?）

因为hash基本上是在高16位进行hash运算

3.为何HashMap中容量均为2^n ?

(n - 1) & hash : 当n为2^n，此时的位运算就相当于 hash % (n-1)

put源码

public V put(K key, V value) {
        return putVal(hash(key), key, value, false, true);
    }
 
 
 
 
final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
                   boolean evict) {
        Node<K,V>[] tab; Node<K,V> p; int n, i;
        // 当前哈希表还未初始化
        if ((tab = table) == null || (n = tab.length) == 0)
            // resize()完成哈希表的初始化操作
            n = (tab = resize()).length;
        // 根据key值hash后的得到桶下标，并且此时的桶中元素个数为空
        if ((p = tab[i = (n - 1) & hash]) == null)
            // 将要保存的节点放置在此桶的第一个元素
            tab[i] = newNode(hash, key, value, null);
        else {
            Node<K,V> e; K k;
            // 节点处于同一个桶中头结点并且key值完全一样，替换头结点
            if (p.hash == hash &&
                ((k = p.key) == key || (key != null && key.equals(k))))
                e = p;
            // hash(key)桶中元素不为空，判断此桶是否已经树化
            else if (p instanceof TreeNode)
                // 调用树化后的方法将新节点添加到红黑树中
                e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
            // 桶中元素不为空并且还是链表
            else {
                for (int binCount = 0; ; ++binCount) {
                    if ((e = p.next) == null) {
                        // 将新节点链到链表尾部
                        p.next = newNode(hash, key, value, null);
                        if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
                            treeifyBin(tab, hash);
                        break;
                    }
                    if (e.hash == hash &&
                        ((k = e.key) == key || (key != null && key.equals(k))))
                        break;
                    p = e;
                }
            }
            if (e != null) { // existing mapping for key
                V oldValue = e.value;
                if (!onlyIfAbsent || oldValue == null)
                    e.value = value;
                afterNodeAccess(e);
                return oldValue;
            }
        }
        ++modCount;
        // 判断添加元素之后整个的哈希表大小是否超过threshold
        if (++size > threshold)
            // 若超过，调用resize()方法扩容
            resize();
        afterNodeInsertion(evict);
        return null;
    }
HashMap的resize()源码
1）负责哈希表的初始化操作

2）当表中元素个数达到阈值: 容量 * 负载因子后进行扩容为原哈希表的二倍(扩容的是桶的个数)

3） 扩容后，原来元素进行rehash:(HashMap最大的开销)，要么元素还待在原桶中，要么待在double 桶中

resize()：
final Node<K,V>[] resize() {
        Node<K,V>[] oldTab = table;
        int oldCap = (oldTab == null) ? 0 : oldTab.length;
        int oldThr = threshold;
        int newCap, newThr = 0;
        if (oldCap > 0) {
            // 当前哈希表已经达到最大值
            if (oldCap >= MAXIMUM_CAPACITY) {
                threshold = Integer.MAX_VALUE;
                return oldTab;
            }
            // 扩容为原hash表二倍
            else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
                     oldCap >= DEFAULT_INITIAL_CAPACITY)
                newThr = oldThr << 1; // double threshold
        }
        else if (oldThr > 0) // initial capacity was placed in threshold
            newCap = oldThr;
        // 进行哈希表的初始化操作
        else {               // zero initial threshold signifies using defaults
            newCap = DEFAULT_INITIAL_CAPACITY;
            newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
        }
        if (newThr == 0) {
            float ft = (float)newCap * loadFactor;
            newThr = (newCap < MAXIMUM_CAPACITY && ft < (float)MAXIMUM_CAPACITY ?
                      (int)ft : Integer.MAX_VALUE);
        }
        threshold = newThr;
        @SuppressWarnings({"rawtypes","unchecked"})
            Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
        table = newTab;
        // 对原hash表的元素进行rehash
        if (oldTab != null) {
            for (int j = 0; j < oldCap; ++j) {
                Node<K,V> e;
                if ((e = oldTab[j]) != null) {
                    oldTab[j] = null;
                    if (e.next == null)
                        newTab[e.hash & (newCap - 1)] = e;
                    else if (e instanceof TreeNode)
                        ((TreeNode<K,V>)e).split(this, newTab, j, oldCap);
                    else { // preserve order
                        Node<K,V> loHead = null, loTail = null;
                        Node<K,V> hiHead = null, hiTail = null;
                        Node<K,V> next;
                        do {
                            next = e.next;
                            if ((e.hash & oldCap) == 0) {
                                if (loTail == null)
                                    loHead = e;
                                else
                                    loTail.next = e;
                                loTail = e;
                            }
                            else {
                                if (hiTail == null)
                                    hiHead = e;
                                else
                                    hiTail.next = e;
                                hiTail = e;
                            }
                        } while ((e = next) != null);
                        if (loTail != null) {
                            loTail.next = null;
                            newTab[j] = loHead;
                        }
                        if (hiTail != null) {
                            hiTail.next = null;
                            newTab[j + oldCap] = hiHead;
                        }
                    }
                }
            }
        }
        return newTab;
    }
重复元素的比较：
TreeSet和TreeMap依靠Comparator或Comparable接口来区分重复元素。

自定义类要想保存在TreeSet和TreeMap中：

1.要么该类直接实现Comparable接口，覆写compareTo方法

2.要么实现一个比较器传入TreeSet或TreeMap来进行外部比较。

返回0认为元素是重复的

 

而HashSet与HashMap并不依赖比较接口comparable或comparator。此时要想区分自定义元素是否重复，需要同时覆写equals与HashCode方法。
为什么hashset要同时覆写这两个方法？

因为在list里只覆写equals方法就可以区分元素是否重复，
因为底层数据结构不一样，list底层是单个元素，她是线性的结构，都在一行保存着，就是一个链表，所有元素都在这这个链表上放着。
而Hashset或者HashMap,底层是哈希表+红黑树。如果有两个元素的值相同，所以他们的哈希码也相同，哈希码确定这个元素应该放在哪个桶下边，但是存放时会检查元素是否与重复
先调用HashCode计算出哈希码，计算出对象存放的数据桶，然后调用equals比较桶里元素是否相等，若相等，则不在存放元素。如果equals返回false，则在相同的桶之后使用链表将元素连接起来。
HashMap 的性能表现非常依赖于哈希码的有效性
所以需要了解HashCode 和 equals 的一些基本约定：
HashCode：求哈希码

equals相等的两个对象，HashCode一定保证相等。HashCode相同的两个对象，equals不一定相等。
重写了 hashCode 也要重写 equals。
hashCode 需要保持一致性，状态改变返回的哈希值仍然要一致。
equals 的对称、反射、传递等特性。
覆写equals方法的原则：(首先覆写equals方法来判断两个元素的内容是否相等。)

自返性	对于任何非空引用值,x，x.equals（x）都返回true
对称性	对于任何非空X,Y，当且仅当x.equals(y)返回true，y.equals(x)也返回true
传递性	对于任何非空x,y,z,如果x.equals(y)返回true，y.equals(z)返回true，一定有x.equlas(z)返回true。
一致性	对于任何非空x,y，若X与Y中的属性没有发生变化，则多次调用x.equals(y)始终返回true或false。
非空性	对于任何非空引用x.equals(null)，一定返回false。
equals比较的是地址

如何证明两个对象真正的相等

若两个对象equals方法返回true，他们的hashCode必然要保持相等。但是两个对象的hashCode相等，equals不一定相等。当且仅当equals与hashCode方法均返回true，才默认两个对象真正的相等。
为什么两个对象的equals相等HashCode却不一定相等

hash是一个函数，算出来是一个值。
下边两个虽然值一样，但是他们不是同一个对象。所以HashCode相等。equals不一定相等。
但是equals比较的是值，所以HashCode也一定相等
name        age
28          2
2           28
“张三”      20
“张三”      20
HashMap 和HashSet如何存放数据：(哈希表的存放原理)

先调用HashCode 计算出对象Hash码决定存放的数据桶，然后使用equals来比较元素是否相等。若相等，则不再放置数据元素。若equals返回false，则在相同的桶之后，使用链表将若干元素链接起来。

Object类：提供的hashcode的方法默认使用对象的地址进行hash。

hash表的意义，为何要分桶来存放元素？

优化了查找次数
--------------------- 
作者：qq_40955824 
来源：CSDN 
原文：https://blog.csdn.net/qq_40955824/article/details/90473406 
版权声明：本文为博主原创文章，转载请附上博文链接！