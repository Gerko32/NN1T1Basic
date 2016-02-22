package T1Base.Parser;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Mapper<T> {
	private Map<String, T> mapping;
	private Map<Integer, T> IDmapping;
	private int lastID;
	private Functor<T> functor;
	public Mapper(Functor<T> functor){
		this.mapping=new HashMap<String, T>();
		this.IDmapping=new HashMap<Integer, T>();
		this.lastID=0;
		this.functor=functor;
	}
	public T Map(String input){
		if(this.mapping.containsKey(input)) return this.mapping.get(input);
		T instance=functor.createInstance(++this.lastID, input);
		this.mapping.put(input, instance);
		this.IDmapping.put(this.lastID, instance);
		return this.mapping.get(input);
	}
	public T GetByID(int id){
		return this.IDmapping.get(id);
	}
	public Collection<T> GetValues(){
		return this.mapping.values();
	}
}
