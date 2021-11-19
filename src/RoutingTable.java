import java.util.ArrayList;

public class RoutingTable {
	public ArrayList<ArrayList<byte[]>> routingTable = new ArrayList<ArrayList<byte[]>>();
	
	public void add(byte[] dst, byte[] netmask, byte[] gateway, byte[] flag, byte[] itf) { // routing table 추가
		ArrayList<byte[]> item = new ArrayList<byte[]>();
		item.add(dst);
		item.add(netmask);
		item.add(gateway);
		item.add(flag);
		item.add(itf);
		this.routingTable.add(item);
	}
	public void remove() { // rounting table 삭제
		
	}
	public void getter() { // 일치하는 gateway 반환
		
	}
}
