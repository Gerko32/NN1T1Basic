package T1Base.Parser;

import java.util.Collection;

public class SentencePropertyMapper {
	private Mapper<EntityType> entityMapper;
	private Mapper<POSTag> posTagMapper;
	
	public SentencePropertyMapper(){
		this.entityMapper=new Mapper<EntityType>(new Functor<EntityType>(){
			public EntityType createInstance(int id, String text) {
				return new EntityType(id, text);
			}
		});
		this.posTagMapper=new Mapper<POSTag>(new Functor<POSTag>(){
			public POSTag createInstance(int id, String text) {
				return new POSTag(id, text);
			}
		});
	}
	
	public EntityType getEntity(String entity){
		return this.entityMapper.Map(entity);
	}
	public EntityType getEntity(int entityID){
		return this.entityMapper.GetByID(entityID);
	}
	public POSTag getPOSTag(String tag){
		return this.posTagMapper.Map(tag);
	}

	public Collection<POSTag> getPOSTags() {
		return this.posTagMapper.GetValues();
	}
}
