import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

public class IPLayer implements BaseLayer {
	public int nUpperLayerCount = 0;
	public String pLayerName = null;
	public BaseLayer p_UnderLayer = null;
	public ArrayList<BaseLayer> p_aUpperLayer = new ArrayList<BaseLayer>();
	public IPLayer secondeIPLayer;
	public RoutingTable RT;
	private class _IP_ADDR {
		private byte[] addr = new byte[4];

		public _IP_ADDR() {
			this.addr[0] = (byte) 0x00;
			this.addr[1] = (byte) 0x00;
			this.addr[2] = (byte) 0x00;
			this.addr[3] = (byte) 0x00;
		}
	}

	private class _IP_HEADER {
		byte ip_verlen;		// 가변적인 header의 길이
		byte ip_tos;		// 서비스의 우선순위
		byte[] ip_len;		// 전체 ip 패킷의 길이
		byte[] ip_id;		// 데이터의 조각화된 패킷을 구분
		byte[] ip_fragoff;	// 단편화된 패킷의 원래 위치 표현
		byte ip_ttl;		// 패킷 전송 시 거칠 수 있는 hop 수(router 수)
		byte ip_proto;		// 상위 프로토콜
		byte[] ip_cksum;	
		_IP_ADDR ip_src;
		_IP_ADDR ip_dst;
		byte[] ip_data;

		public _IP_HEADER() {
			this.ip_src = new _IP_ADDR();
			this.ip_dst = new _IP_ADDR();
			this.ip_len = new byte[2];
			this.ip_id = new byte[2];
			this.ip_fragoff = new byte[2];
			this.ip_cksum = new byte[2];
			this.ip_data = null;
		}
	}

	_IP_HEADER m_sHeader = new _IP_HEADER();

	public IPLayer(String pName) {
		// super(pName);
		// TODO Auto-generated constructor stub
		pLayerName = pName;
		ResetHeader();
	}
	
	public void ResetHeader() {
		for (int i = 0; i < 4; i++) {
			m_sHeader.ip_dst.addr[i] = (byte) 0x00;
			m_sHeader.ip_src.addr[i] = (byte) 0x00;
		}
		for (int i=0; i<2; i++) {
			m_sHeader.ip_len[i] = (byte) 0x00;
			m_sHeader.ip_id[i] = (byte) 0x00;
			m_sHeader.ip_fragoff[i] = (byte) 0x00;
			m_sHeader.ip_cksum[i] = (byte) 0x00;
		}
		m_sHeader.ip_verlen = (byte) 0x00;;
		m_sHeader.ip_tos = (byte) 0x00;;
		m_sHeader.ip_ttl = (byte) 0x00;;
		m_sHeader.ip_proto = (byte) 0x00;;
		m_sHeader.ip_data = null;
	}
	
	public _IP_ADDR GetIPDstAddress() {
		return m_sHeader.ip_dst;
	}

	public _IP_ADDR GetIPSrcAddress() {
		return m_sHeader.ip_src;
	}
	
	public void SetIpDstAddress(byte[] input) {
		for (int i = 0; i < 4; i++) {
			m_sHeader.ip_dst.addr[i] = input[i];
		}
	}

	public void SetIpSrcAddress(byte[] input) {
		for (int i = 0; i < 4; i++) {
			m_sHeader.ip_src.addr[i] = input[i];
		}
	}
	
	public void ARPSend(byte[] src, byte[] dst) {
		this.SetIpSrcAddress(src);
		this.SetIpDstAddress(dst);
		((ARPLayer) this.GetUnderLayer()).ARPSend(src, dst);
	}
	
	public void receive(byte[] input) { //패킷 수신
		byte[] dst_ip = new byte[4];
		System.arraycopy(input, 16, dst_ip, 0, 4);
		byte[] src_ip = new byte[4];
		System.arraycopy(input, 12, src_ip, 0, 4);
		
		int idx = this.RT.matchEntry(dst_ip);
		ArrayList<byte[]> temp = RT.getEntry(idx);
		// 0:dst 1:netmask 2:gateway 3:flag 4:interface
		
		_IP_ADDR dst = new _IP_ADDR();
		byte[] flag = temp.get(3);
		if(flag[0] == 1 & flag[1] == 0 & flag[2]==1) { //UH
			byte[] mac = ((ARPLayer) this.GetUnderLayer()).checkCacheTable(dst_ip); // src_ip가 뭐지?
			
			for(int i=0; i<4; i++) {
				dst.addr[i] = dst_ip[i]; 
			}
			if(Arrays.equals(mac, src_ip)) { // ARP로 mac주소 요청받아와야 하는 경우
				return;
			}else {
				this.send();
			}
		}
		else if(flag[0] == 1 & flag[1] == 1 & flag[2]==0) { //UG
			byte[] mac = ((ARPLayer) this.GetUnderLayer()).checkCacheTable(temp.get(2)); // src_ip가 뭐지?
			for(int i=0; i<4; i++) {
				dst.addr[i] = temp.get(2)[i]; 
			}
			if(Arrays.equals(mac, src_ip)) { // ARP로 mac주소 요청받아와야 하는 경우
				return;
			}else {
				this.send();
			}
		}
	}
	
	public void settingFrame(byte[] input) {
	}
	
	public void send() { //패킷 송신
		
	}

	public void addRoutingTable(byte[] dst, byte[] netmask, byte[] gateway, byte[] flag, byte[] itf) {
		this.RT.add(dst, netmask, gateway, flag, itf);
	}
	
	public void removeRoutingTable() {
		
	}
	public void setRouter(RoutingTable routingtable) {
		this.RT = routingtable;
	}
	public void secondIPLayerSet(IPLayer ip_layer) {
		this.secondeIPLayer = ip_layer;
	}
	
//	public byte[] ObjToByte(_IP_HEADER Header, byte[] input, int length) {//data
//		byte[] buf = new byte[length + 14];
//		for(int i = 0; i < 4; i++) {
//			buf[i] = Header.ip_dst.addr[i];
//			buf[i+4] = Header.ip_src.addr[i];
//		}			
//		buf[] = Header.enet_type[0];
//		buf[13] = Header.enet_type[1];
//		for (int i = 0; i < length; i++)
//			buf[14 + i] = input[i];
//
//		return buf;
//	}
//	
//	public boolean Send(byte[] input, int length) {
//		m_sHeader.enet_type = intToByte2(0x2080);
//		m_sHeader.enet_data = input;
//		byte[] bytes = ObjToByte(m_sHeader, input, length);
//		this.GetUnderLayer().Send(bytes, length + 14);
//
//		return true;
//	}
	
	@Override
	public String GetLayerName() {
		return pLayerName;
	}
	@Override
	public BaseLayer GetUnderLayer() {
		if (p_UnderLayer == null)
			return null;
		return p_UnderLayer;
	}
	@Override
	public BaseLayer GetUpperLayer(int nindex) {
		if (nindex < 0 || nindex > nUpperLayerCount || nUpperLayerCount < 0)
			return null;
		return p_aUpperLayer.get(nindex);
	}
	@Override
	public void SetUnderLayer(BaseLayer pUnderLayer) {
		if (pUnderLayer == null)
			return;
		this.p_UnderLayer = pUnderLayer;
	}
	@Override
	public void SetUpperLayer(BaseLayer pUpperLayer) {
		if (pUpperLayer == null)
			return;
		this.p_aUpperLayer.add(nUpperLayerCount++, pUpperLayer);
	}
	@Override
	public void SetUpperUnderLayer(BaseLayer pUULayer) {
		this.SetUpperLayer(pUULayer);
		pUULayer.SetUnderLayer(this);
	}
}
