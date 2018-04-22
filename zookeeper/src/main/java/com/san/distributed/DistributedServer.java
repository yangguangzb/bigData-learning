package com.san.distributed;

import java.io.IOException;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooDefs.Ids;

public class DistributedServer {
	private static final String connectString="192.168.253.21:2181,192.168.253.22:2181,192.168.253.23:2181";
	private static final int sessionTimeout=2000;
	private static final String parentNode="/servers";
	private ZooKeeper zk=null;
	
	/**
	 * 创建到zk客户端的连接
	 * @throws IOException 
	 */
	public void getConnect() throws IOException{
		zk=new ZooKeeper(connectString, sessionTimeout,new Watcher() {
			
			@Override
			public void process(WatchedEvent event) {
				System.out.println(event.getType()+"--"+event.getPath());
				try {
					//重新监听
					zk.getChildren("/", true);
				} catch (Exception e) {
				
				}
			}
		});
	}
	
	/**
	 * 向zk集群注册服务器信息
	 * @param homename：主机ip
	 * @throws InterruptedException 
	 * @throws KeeperException 
	 */
	public void registerServer(String homename) throws KeeperException, InterruptedException{
		String create=zk.create(parentNode+"/server", homename.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
		System.out.println(homename+"   is online..   "+create);
	}
	
	/**
	 * 简单业务功能
	 * @param hostname
	 * @throws InterruptedException 
	 */
	public void handleBussiness(String hostname) throws InterruptedException{
		System.out.println(hostname+"   start working.....  ");
		Thread.sleep(Long.MAX_VALUE);
	}
	
	public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
		//获取zk连接
		DistributedServer server=new DistributedServer();
		server.getConnect();
		
		//利用zk连接注册服务器信息
		server.registerServer(args[0]);
		
		//启动业务
		server.handleBussiness(args[0]);
		
	}
	
}
















