package com.san.zk;
import java.io.IOException;
import java.util.List;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;
public class SimpleZkClient {
	//连接主机
	private static final String connectString="192.168.253.21:2181,192.168.253.22:2181,192.168.253.23:2181";
	//2秒
	private static final int sessionTimeout=2000;
	private ZooKeeper zkClient=null;
	@Before
	public void init() throws IOException{
		zkClient=new ZooKeeper(connectString, sessionTimeout, new Watcher() {
			//收到事件通知后的回调函数
			public void process(WatchedEvent event) {
				System.out.println(event.getType()+"--"+event.getPath());
				try {
					//重新监听
					zkClient.getChildren("/", true);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
	}
	
	/**
	 * 创建数据节点到zk中
	 * @throws InterruptedException 
	 * @throws KeeperException
	 * 参数1：要创建的节点的路径
	 * 参数2：节点的数据(可以是任意类型，但是都要转化为byte[])
	 * 参数3：节点的权限
	 * 参数4：节点的类型
	 * @throws IOException 
	 */
	@Test
	public void testCreate() throws KeeperException, InterruptedException, IOException{
		zkClient.create("/eclipse", "nihao".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
	}
	
	/**
	 * 判断节点是否存在
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	@Test
	public void testExist() throws KeeperException, InterruptedException{
		Stat exists = zkClient.exists("/eclipse", false);
		if(exists==null){
			System.out.println("不存在");
		}else{
			System.out.println("存在");
		}
	}
	
	/**
	 * 获取子节点
	 * @throws InterruptedException 
	 * @throws KeeperException 
	 */
	@Test
	public void getChildren() throws KeeperException, InterruptedException{
		List<String> children = zkClient.getChildren("/", true);
		for(String child : children){
			System.out.println(child);
		}
	}
	
	/**
	 * 获取节点的数据
	 * @throws InterruptedException 
	 * @throws KeeperException 
	 */
	@Test 
	public void getData() throws KeeperException, InterruptedException{
		byte[] data = zkClient.getData("/eclipse", true, null);
		System.out.println(new String(data));
		Thread.sleep(Long.MAX_VALUE);
	}
	
	/**
	 * 删除节点
	 * 参数2：指定删除的版本号，-1表示删除所有版本
	 * @throws KeeperException 
	 * @throws InterruptedException 
	 */
	@Test 
	public void deleteZnode() throws InterruptedException, KeeperException{
		zkClient.delete("/eclipse",-1);
	}
	
	/**
	 * 修改节点数据
	 * @throws InterruptedException 
	 * @throws KeeperException 
	 */
	@Test
	public void setData() throws KeeperException, InterruptedException{
		zkClient.setData("/eclipse", "hello".getBytes(), -1);
		byte[] data = zkClient.getData("/eclipse", false, null);
		System.out.println(new String(data));
	}
}
