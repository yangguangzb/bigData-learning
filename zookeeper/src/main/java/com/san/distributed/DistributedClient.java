package com.san.distributed;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

public class DistributedClient {
	private static final String connectString="192.168.253.21:2181,192.168.253.22:2181,192.168.253.23:2181";
	private static final int sessionTimeout=2000;
	private static final String parentNode="/servers";
	//加volatile关键字修饰
	private volatile List<String> serverList;
	private ZooKeeper zk=null;
	
	/**
	 * 创建连接
	 * @throws IOException 
	 */
	public void getConnection() throws IOException{
		zk=new ZooKeeper(connectString, sessionTimeout, new Watcher() {
			
			@Override
			public void process(WatchedEvent event) {
				try {
					//重新更新服务器列表，并注册监听
					getServerList();
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
	}
	
	/**
	 * 获取服务器列表，并注册监听
	 * @throws InterruptedException 
	 * @throws KeeperException 
	 */
	public void getServerList() throws KeeperException, InterruptedException{
		//获取服务器子节点信息，并对父节点进行监听
		List<String> children=zk.getChildren(parentNode, true);
		
		//先创建一个局部的list来存储服务器信息
		List<String> sersers=new ArrayList<String>();
		for(String child : children){
			byte[] data = zk.getData(parentNode+"/"+child, false, null);
			sersers.add(new String(data));
		}
		//把servers赋值给成员变量serverList,提供给各业务线程使用
		serverList=sersers;
		//打印服务器列表
		System.out.println(serverList);
	}
	
	/**
	 * 简单业务功能
	 * @throws InterruptedException 
	 */
	public void handleBussiness() throws InterruptedException{
		System.out.println("client start working ....");
		Thread.sleep(Long.MAX_VALUE);
	}
	
	public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
		//获取连接
		DistributedClient client=new DistributedClient();
		client.getConnection();
		
		//获取servers的字节点信息，并注册监听
		client.getServerList();
		
		//启动业务
		client.handleBussiness();
		
	}
}
