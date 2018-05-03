package com.san.hdfs;
import java.net.URI;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.Before;
import org.junit.Test;
public class HdfsClientDemo {
	FileSystem fs=null;
	Configuration conf=null;
	
	@Before
	public void init() throws Exception{
		conf=new Configuration();
		fs=FileSystem.get(new URI("http://mini01:9000"), conf,"hadoop");
	}
	
	/**
	 * 创建目录
	 */
	@Test
	public void mkdirTest() throws Exception{
		boolean mkdirs = fs.mkdirs(new Path("/aaa"));
		System.out.println("mkdirs="+mkdirs);
	}
}

