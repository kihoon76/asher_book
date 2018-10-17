package net.asher.book.domain;

import org.apache.ibatis.type.Alias;

@Alias("LogSend")
public class LogSend {

	private String targetIdx;
	private String txMsg;
	private String rxMsg;
	private String isErr;
	private String writeDate;
	private String type;			//E: email S:sms
	private String msgId;			//sms일 경우 고유번호
	
	public String getTargetIdx() {
		return targetIdx;
	}
	public void setTargetIdx(String targetIdx) {
		this.targetIdx = targetIdx;
	}
	public String getTxMsg() {
		return txMsg;
	}
	public void setTxMsg(String txMsg) {
		this.txMsg = txMsg;
	}
	public String getRxMsg() {
		return rxMsg;
	}
	public void setRxMsg(String rxMsg) {
		this.rxMsg = rxMsg;
	}
	public String getIsErr() {
		return isErr;
	}
	public void setIsErr(String isErr) {
		this.isErr = isErr;
	}
	public String getWriteDate() {
		return writeDate;
	}
	public void setWriteDate(String writeDate) {
		this.writeDate = writeDate;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
}
