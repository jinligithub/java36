Vector
Verctor 是 Java 早期提供的线程安全的动态数组，如果不需要线程安全，并不建议选择，毕竟同步是有额外开销的。Vector 内部是使用对象数组来保存数据，可以根据需要自动的增加容量，当数组已满时，会创建新的数组，并拷贝原有数组数据。

 

ArrayList
ArrayList 是应用更加广泛的动态数组实现，它本身不是线程安全的，所以性能要好很多。与Vector 近似，ArrayList 也是可以根据需要调整容量，不过两者的调整逻辑有所区别，Vector在扩容时会提高 1 倍，而 ArrayList 则是增加 50%。
 

LinkedList
LinkedList 顾名思义是 Java 提供的双向链表，所以它不需要像上面两种那样调整容量，它也不是线程安全的。

**********************ArrayList，Vector，LinkedList的区别******************
1.ArrayList，Vector的区别：

1.出现版本：
ArrayList  JDK1.2

Vector  JDK1.0 （出现在List，Collection接口之前）

2.初始化策略区别
Vector在无参构造执行后将对象数组大小初始化为10.

ArrayList采用懒加载策略，在构造方法阶段并不初始化对象数组，在第一次添加元素时才初始化对象数组大小为10

//--->ArrayList 
private static final int DEFAULT_CAPACITY = 10;


//Vector
 public Vector() {
        this(10);
    }
3.扩容策略
ArrayList扩容时，新数组大小变为原数组的1.5倍。

Vector扩容时，新数组大小变为原数组的2倍。

//ArrayList
 private void grow(int minCapacity) {
        // overflow-conscious code
        int oldCapacity = elementData.length;
        //新数组
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        if (newCapacity - minCapacity < 0)
            newCapacity = minCapacity;
        if (newCapacity - MAX_ARRAY_SIZE > 0)
            newCapacity = hugeCapacity(minCapacity);
        // minCapacity is usually close to size, so this is a win:
        elementData = Arrays.copyOf(elementData, newCapacity);
    }
//​Vector
 private void grow(int minCapacity) {
        // overflow-conscious code
        int oldCapacity = elementData.length;
        //新数组
        int newCapacity = oldCapacity + ((capacityIncrement > 0) ?
                                         capacityIncrement : oldCapacity);
        if (newCapacity - minCapacity < 0)
            newCapacity = minCapacity;
        if (newCapacity - MAX_ARRAY_SIZE > 0)
            newCapacity = hugeCapacity(minCapacity);
        elementData = Arrays.copyOf(elementData, newCapacity);
    }

​
 

4.线程安全性
ArrayList采用异步处理，线程不安全，效率较高

Vector采用在方法上加锁（Synchronized），线程安全，效率较低。（即使要使用线程安全的List，也不用Vector）

ArrayList、LinkedList为非线程安全；Vector是基于synchronized实现的线程安全的ArrayList。

需要注意的是：

单线程应尽量使用ArrayList，Vector因为同步会有性能损耗；即使在多线程环境下，我们可以利用Collections这个类中为我们提供的synchronizedList(List list)方法返回一个线程安全的同步列表对象

 

5.遍历
Vector支持较老的迭代器Enumeration，（支持：Iterator 、 ListIterator 、 foreach 、Enumeration）

ArrayList不支持：（支持：Iterator 、 ListIterator 、 foreach）

6.读写机制
ArrayList在执行插入元素是超过当前数组预定义的最大值时，数组需要扩容，扩容过程需要调用底层System.arraycopy()方法进行大量的数组复制操作；在删除元素时并不会减少数组的容量（如果需要缩小数组容量，可以调用trimToSize()方法）；在查找元素时要遍历数组，对于非null的元素采取equals的方式寻找。

LinkedList在插入元素时，须创建一个新的Entry对象，并更新相应元素的前后元素的引用；在查找元素时，需遍历链表；在删除元素时，要遍历链表，找到要删除的元素，然后从链表上将此元素删除即可。

 

2.ArrayList，Vector的共同点：

底层均使用数组实现

 

3.ArrayList和LinkedList

 1. 观察ArrayList源码,可以发现ArrayList里面存放的是一个数组,如果实例化此类对象时传入了数组大小,则 里面保存的数组就会开辟一个定长的数组,但是后面再进行数据保存的时候发现数组个数不够了会进行数 组动态扩充. 所以在实际开发之中,使用ArrayList最好的做法就是设置初始化大小.。ArrayList底层使用数组实现

2. LinkedList：是一个纯粹的链表实现,与之前编写的链表程序的实现基本一样(人家性能高)，.LinkedList底层使用双向链表实

3.读写效率
ArrayList对元素的增加和删除都会引起数组的内存分配空间动态发生变化。因此，对其进行插入和删除速度较慢，但检索速度很快。
LinkedList由于基于链表方式存放数据，增加和删除元素的速度较快，但是检索速度较慢。

 

补充：

集合框架类图



List，set和Queue的适用场景：

List，也就是我们前面介绍最多的有序集合，它提供了方便的访问、插入、删除等操作

Set，Set 是不允许重复元素的，这是和 List 最明显的区别，也就是不存在两个对象 equals返回 true。我们在日常开发中有很多需要保证元素唯一性的场合。

Queue/Deque，则是 Java 提供的标准队列结构的实现，除了集合的基本功能，它还支持类似先入先出（FIFO， First-in-First-Out）或者后入先出（LIFO，Last-In-First-Out）等特定行为。这里不包括 BlockingQueue，因为通常是并发编程场合，所以被放置在并发包

 

Set接口与List接口最大的不同在于Set接口中的内容是不允许重复的。同时需要注意的是，Set接口并没有对Collection接口进行扩充，而List对Collection进行了扩充。因此，在Set接口中没有get()方法。

Set 的适用场景 

TreeSet 支持自然顺序访问,但是添加,删除,包含等操作要相对低效(log(n) 时间). 

HashSet 则是利用哈希算法,理想情况下,如果哈希散列正常,可以提供常数时间的添加, 删除,包含等操作,但是它不保证有序. 

LinkedHashSet,内部构建了一个记录插入顺序的双向链表,因此提供了按照插入顺序遍历 的能力,与此同时,也保证了常数时间的添加,删除,包含等操作,这些操作性能略低于 HashSet,因为需要维护链表的开销. 

在遍历元素时,HashSet 性能受自身容量影响,所以初始化时,除非有必要,不然不要将其 背后的 HashMap 容量设置过大.而对于 LinkedHashSet,由于其内部链表提供的方便,遍 历性能只和元素多少有关系

Set接口不允许数据重复（Set接口就是value值相同的Map集合，先有Map才有Set）。Set接口没有扩充方法

 

Set接口的常用子类：

 

HashSet（无语存储）---HashMap

1.底层使用哈希表+红黑树
2.允许存放null，无序存储
TreeSet（有序存储）：--TreeMap

1.底层使用红黑树
2.不允许出现空值，有序存储。
3.自定义类要想保存到TreeSet中，要么实现Comparable接口，要么向TreeSet中传入一个比较器（Compartor接口）
 

在java中，若想实现自定义类的比较，提供了以下两个接口：Comparable，Compartor

 

java.lang.Comparable接口（内部比较器）

若一个类实现了Comparable接口，就意味着该类支持排序。
存放该类的Conllection或数组，可以直接通过Collections.sort()  或Arrays.sort()进行排序。
实现了Comparable接口的类可以直接存放在TreeSet或TreeMap中
public int  compareTo （T  o  ）

返回值的三种情况：

返回正数：表示当前对象大于目标对象
返回0：表示当前对象等于目标对象
返回负数：表示当前对象小于目标对象
 

Compartor（外部排序结构）

定义：若要控制某个自定义类的顺序，而该类本身不支持排序（类本身没有实现Compartor接口）。我们可以建立一个该类的“比较器”来进行排序。比较器实现Compartor接口即可。

“比较器”：实现了Compartor接口的类作为比较器，通过比较器来进行类的排序。

int compare（T  o1,  T    o2）;

返回值与compare返回值一样。

返回正数表示：o1>o2
返回0表示：o1=o2
返回小数表示：o1<o2
实现了Compartor接口进行第三方排序-策略模式，此方法更加灵活，可以轻松改变策略进行第三方的排序算法。

 

Compartor接口与Compartor接口的区别：

Comparable相当于“内部比较器”，而Comparator相当于“外部比较器”。
Comparable是排序接口，若一个 类实现了Comparable接口，意味着该类支持排序，是一个内部就比较器（自己和别人比）
Compartor接口时比较器接口，本身不支持排序，专门有若干个第三方的比较器（实现了Compartor接口的类）来进行类的排序，是一个外部比较器（策略模式）
TreeSet子类可以进行排序，所以我们可以利用TreeSet实现数据的排列处理操作。此时要想进行排序实际上是针对于对象数组进行的排序处理，而如果要进行对象数组的排序，对象所在的类一定要实现Comparable接口并且覆写compareTo()方法，只有通过此方法才能知道大小关系。

需要提醒的是：如果现在是用Comparable接口进行大小关系匹配，所有属性必须全部进行比较操作

 

重复元素的比较：

TreeSet和TreeMap依靠Comparator或Comparable接口来区分重复元素。

自定义类要想保存在TreeSet和TreeMap中：

1.要么该类直接实现Comparable接口，覆写compareTo方法

2.要么实现一个比较器传入TreeSet或TreeMap来进行外部比较。

注意：返回0认为元素是重复的

 

而HashSet与HashMap并不依赖比较接口comparable或comparator。此时要想区分自定义元素是否重复，需要同时覆写equals与HashCode方法

看到的一个问题：

1.理解 Java 提供的默认排序算法，具体是什么排序方式以及设计思路等

思路：

1.首先需要区分是 Arrays.sort() 还是 Collections.sort() （底层是调用 Arrays.sort()）；什么数据类型；多大的数据集（太小的数据集，复杂排序是没必要的，Java 会直接进行二分插入排序）等

对于原始数据类型，目前使用的是所谓双轴快速排序（Dual-Pivot QuickSort：两个基准值），是一种改进的快速排序算法，早期版本是相对传统的快速排序
对于双轴排序这里面讲的很好：https://blog.csdn.net/Holmofy/article/details/71168530

而对于对象数据类型，目前则是使用TimSort，思想上也是一种归并和二分插入排序（binarySort）结合的优化排序算法。TimSort 并不是 Java 的独创，简单说它的思路是查找数据集中已经排好序的分区（这里叫 run），然后合并这些分区来达到排序的目
解释：

TimSort 是一个归并排序做了大量优化的版本。对归并排序排在已经反向排好序的输入时做了特别优化。对已经正向排好序的输入减少回溯。对两种情况混合（一会升序，一会降序）的输入处理比较好。