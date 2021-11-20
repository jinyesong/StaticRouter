import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

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
		this.routingTable.remove(this.routingTable.size()-1);
	}
//	public ArrayList<byte[]> getEntry(int idx) { // 일치하는 gateway 반환
//		return this.routingTable.get(idx);
//	}
//	
//	public int size() {
//		return this.routingTable.size();
//	}
	
	public byte[] subnetting(byte[] dst_ip, byte[] netmask) {
	      byte[] network_address = new byte[4];
	      for(int i = 0; i < 4; i++) {
	         network_address[i] = (byte) (dst_ip[i] & netmask[i]);
	      }
	      //System.out.println(Byte.toUnsignedInt(dst_ip[0]) + "." + Byte.toUnsignedInt(dst_ip[1]) + "." + Byte.toUnsignedInt(dst_ip[2])  + "." + Byte.toUnsignedInt(dst_ip[3]));
	      //System.out.println(Byte.toUnsignedInt(netmask[0]) + "." + Byte.toUnsignedInt(netmask[1]) + "." + Byte.toUnsignedInt(netmask[2])  + "." + Byte.toUnsignedInt(netmask[3]));
	      //System.out.println(Byte.toUnsignedInt(network_address[0]) + "." + Byte.toUnsignedInt(network_address[1]) + "." + Byte.toUnsignedInt(network_address[2])  + "." + Byte.toUnsignedInt(network_address[3]));
	      return network_address;
	   }
	
	public int matchEntry(byte[] dst_ip) {
		int matchIdx = this.routingTable.size()-1;
		byte[] matchIp = this.routingTable.get(this.routingTable.size()-1).get(0);
		for(int i=0; i<this.routingTable.size()-1; i++) {
			ArrayList<byte[]> temp = this.routingTable.get(i);
			// 0:dst 1:netmask 2:gateway 3:flag 4:interface
			byte[] result_ip = this.subnetting(dst_ip, temp.get(1));
			if(Arrays.equals(temp.get(0), result_ip)) {
				if(ByteBuffer.wrap(matchIp).getInt() < ByteBuffer.wrap(result_ip).getInt()) {
					matchIdx = i;
					matchIp = result_ip;
				}
			}
		}
		return matchIdx;
	}
}
