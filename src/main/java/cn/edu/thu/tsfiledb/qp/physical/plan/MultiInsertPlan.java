package cn.edu.thu.tsfiledb.qp.physical.plan;

import java.util.ArrayList;
import java.util.List;

import cn.edu.thu.tsfile.common.exception.ProcessorException;
import cn.edu.thu.tsfile.timeseries.read.qp.Path;
import cn.edu.thu.tsfiledb.qp.exec.QueryProcessExecutor;
import cn.edu.thu.tsfiledb.qp.logical.operator.Operator.OperatorType;

/**
 * given a insert plan and construct a {@code InsertPlan}
 * 
 * @author kangrong
 *
 */
public class MultiInsertPlan extends PhysicalPlan {
	private String deltaObject;
	private List<String> measurementList;
	private List<String> insertValues;
    private long insertTime;
    
    
    //insertType : 1表示插入到Bufferwrite,2表示插入到Overflow
    private int insertType;
    
    public MultiInsertPlan() {
        super(false, OperatorType.MULTIINSERT);
    }

    public MultiInsertPlan(String deltaObject, long insertTime, List<String> measurementList, List<String> insertValues) {
        super(false, OperatorType.MULTIINSERT);
        this.insertTime = insertTime;
        this.deltaObject = deltaObject;
        this.measurementList = measurementList;
        this.insertValues = insertValues;
    }

    public MultiInsertPlan(int insertType, String deltaObject, long insertTime, List<String> measurementList, List<String> insertValues) {
        super(false, OperatorType.MULTIINSERT);
        this.insertType = insertType;
        this.insertTime = insertTime;
        this.deltaObject = deltaObject;
        this.measurementList = measurementList;
        this.insertValues = insertValues;
    }

    @Override
    public boolean processNonQuery(QueryProcessExecutor exec) throws ProcessorException{
		insertType = exec.multiInsert(deltaObject, insertTime, measurementList, insertValues);
        return true;
    }

    public long getTime() {
        return insertTime;
    }

    public void setTime(long time) {
        this.insertTime = time;
    }

    @Override
    public List<Path> getInvolvedSeriesPaths() {
        List<Path> ret = new ArrayList<>();
        
        for(String m : measurementList){
        	StringBuilder sb = new StringBuilder();
        	sb.append(deltaObject);
        	sb.append(".");
        	sb.append(m);
        	Path p = new Path(sb.toString());
        	ret.add(p);
        }
        return ret;
    }

	public int getInsertType() {
		return insertType;
	}

	public void setInsertType(int insertType) {
		this.insertType = insertType;
	}

	public String getDeltaObject() {
        return this.deltaObject;
    }

    public void setDeltaObject(String deltaObject) {
        this.deltaObject = deltaObject;
    }

	public List<String> getMeasurementList() {
        return this.measurementList;
    }

    public void setMeasurementList(List<String> measurementList) {
        this.measurementList = measurementList;
    }

    public List<String> getInsertValues() {
        return this.insertValues;
    }

    public void setInsertValues(List<String> insertValues) {
        this.insertValues = insertValues;
    }
}
