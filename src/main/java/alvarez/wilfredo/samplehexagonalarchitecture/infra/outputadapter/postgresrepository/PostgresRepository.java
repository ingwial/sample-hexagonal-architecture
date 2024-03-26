package alvarez.wilfredo.samplehexagonalarchitecture.infra.outputadapter.postgresrepository;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import alvarez.wilfredo.samplehexagonalarchitecture.infra.outputport.EntityRepository;

@Component
@RequiredArgsConstructor
public class PostgresRepository implements EntityRepository {
    private final Logger logger = LoggerFactory.getLogger(PostgresRepository.class);

    private final JdbcTemplate jdbcTemplate;

    @Override
    public <T> T save(T reg) {

        Field[] entityFields = reg.getClass().getDeclaredFields();

        String[] fields = new String[ entityFields.length ];
        Object[] fieldValues = new Object[ entityFields.length ];

        try {
            for ( int i=0; i<entityFields.length; i++ ) {
                fields[i] = entityFields[i].getName();
                fieldValues[i] = reg.getClass()
                    .getMethod( "get"+entityFields[i].getName().substring(0,1).toUpperCase()+entityFields[i].getName().substring(1) )
                    .invoke( reg );
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
                 | NoSuchMethodException | SecurityException e
        ) {
                logger.error(e.getMessage());
        }

        String sql = "INSERT INTO " +
                reg.getClass().getSimpleName() +
                "(" + String.join(",", fields) + ")" +
                " VALUES " +
                "(" + String.join(",", Collections.nCopies(fields.length, "?")) + ")";

        jdbcTemplate.update(sql, fieldValues);

        return reg;
    } 

    @Override
    public <T> T getById(String id, Class<T> clazz) {
        List<T> list = jdbcTemplate.query("SELECT * FROM "+clazz.getSimpleName()+" WHERE id = ?", 
            new LombokRowMapper<T>( clazz ), 
            id );

        if ( !list.isEmpty() ) return list.get(0);

        return null;
    }

    @Override
    public <T> List<T> getAll(Class<T> clazz) {
        return jdbcTemplate.query("SELECT * FROM "+clazz.getSimpleName(), new LombokRowMapper<T>( clazz ) );
    }

    private class LombokRowMapper<T> implements RowMapper<T> {
        private final Class<?> clazz;

        public LombokRowMapper( Class<?> clazz ) {
            this.clazz = clazz;
        }
        
        @Override
        public T mapRow(ResultSet rs, int rowNum) throws SQLException {

            try {
                Method builderMethod = clazz.getMethod("builder");
                Object row = builderMethod.invoke(null);
                Method[] m = row.getClass().getDeclaredMethods();

                for (Method method : m) {
                    int pos = getPos(rs, method);
                    if (pos != -1) {
                        Object fieldValue = rs.getObject(pos);
                        method.invoke(row, fieldValue);
                    }
                }
                return (T) row.getClass().getMethod("build").invoke(row);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
                     | NoSuchMethodException | SecurityException e) {
                logger.error(e.getMessage());
            }

            return null;
        }

    }

    private static int getPos(ResultSet rs, Method method) {
        int pos = -1;
        try { pos = rs.findColumn(method.getName()); } catch (SQLException ignored) { }
        return pos;
    }

}
