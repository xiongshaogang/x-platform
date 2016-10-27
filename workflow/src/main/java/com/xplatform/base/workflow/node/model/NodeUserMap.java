package com.xplatform.base.workflow.node.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.xplatform.base.workflow.core.facade.model.TaskExecutor;
import com.xplatform.base.workflow.node.entity.NodeUserConditionEntity;
import com.xplatform.base.workflow.node.entity.NodeUserEntity;

public class NodeUserMap{
  private String nodeId;//节点id
  private String nodeName;//节点名称
  private String setId;//节点设置id
  private List<NodeUserEntity> nodeUserList=new ArrayList<NodeUserEntity>();//节点用户集合
  private Set<TaskExecutor> taskExecutors = new HashSet<TaskExecutor>();//节点执行着
  private boolean isMultipleInstance;//是否多实例节点
  private List<NodeUserConditionEntity> nodeUserConditionList=new ArrayList<NodeUserConditionEntity>();//用户节点条件
  
  public static short CHOOICETYPE_NO = 0;//不允许选择
  public static short CHOOICETYPE_RADIO = 1;//单选
  public static short CHOOICETYPE_CHECK = 2;//多选n

  private short chooiceType = 0;//选择类型，默认为不允许选择

  public short getChooiceType() {
    return this.chooiceType;
  }

  public void setChooiceType(short chooiceType) {
    this.chooiceType = chooiceType;
  }

  public NodeUserMap(){ }

  public NodeUserMap(String setId, String nodeId, String nodeName, List<NodeUserEntity> nodeUserList){
    this.setId = setId;
    this.nodeId = nodeId;
    this.nodeName = nodeName;
    this.nodeUserList = nodeUserList;
  }

  public NodeUserMap(String nodeId, String nodeName, Set<TaskExecutor> taskExecutors) {
    this.nodeId = nodeId;
    this.nodeName = nodeName;
    this.taskExecutors = taskExecutors;
  }

  public NodeUserMap(String nodeId, String nodeName, Set<TaskExecutor> taskExecutors, boolean isMultipleInstance){
    this.nodeId = nodeId;
    this.nodeName = nodeName;
    this.taskExecutors = taskExecutors;
    this.isMultipleInstance = isMultipleInstance;
  }

  public String getNodeId()
  {
    return this.nodeId;
  }

  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  public String getNodeName() {
    return this.nodeName;
  }

  public void setNodeName(String nodeName) {
    this.nodeName = nodeName;
  }

  public List<NodeUserEntity> getNodeUserList() {
    return this.nodeUserList;
  }

  public void setNodeUserList(List<NodeUserEntity> nodeUserList) {
    this.nodeUserList = nodeUserList;
  }

  public String getSetId(){
    return this.setId;
  }

  public void setSetId(String setId){
    this.setId = setId;
  }

  public Set<TaskExecutor> getTaskExecutors() {
    return this.taskExecutors;
  }

  public void setTaskExecutors(Set<TaskExecutor> users) {
    this.taskExecutors = users;
  }

  public boolean isMultipleInstance() {
    return this.isMultipleInstance;
  }

  public void setMultipleInstance(boolean isMultipleInstance) {
    this.isMultipleInstance = isMultipleInstance;
  }
  public List<NodeUserConditionEntity> getNodeUserConditionList() {
    return this.nodeUserConditionList;
  }

  public void setNodeUserConditionList(List<NodeUserConditionEntity> nodeUserConditionList) {
    this.nodeUserConditionList = nodeUserConditionList;
  }
}
