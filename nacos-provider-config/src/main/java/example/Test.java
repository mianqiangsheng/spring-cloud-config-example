package example;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.PluginException;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.util.MapUtil;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Test {

    static class Signature {

        public Signature(Class<?> type, String method, Class<?>[] args) {
            this.type = type;
            this.method = method;
            this.args = args;
        }

        /**
         * Returns the java type.
         *
         * @return the java type
         */
        Class<?> type;

        /**
         * Returns the method name.
         *
         * @return the method name
         */
        String method;

        /**
         * Returns java types for method argument.
         * @return java types for method argument
         */
        Class<?>[] args;

        public Class<?> type() {
            return type;
        }

        public String method() {
            return method;
        }

        public Class<?>[] args() {
            return args;
        }
    }

    public static void main(String[] args) {

        Signature[] sigs = new Signature[]{ new Signature(Executor.class, "query",new Class[]{MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
                new Signature(Executor.class,"query", new Class[]{MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
                new Signature(Executor.class, "update", new Class[]{MappedStatement.class, Object.class})};

        Map<Class<?>, Set<Method>> signatureMap = new HashMap<>();
        for (Signature sig : sigs) {
            Set<Method> methods = MapUtil.computeIfAbsent(signatureMap, sig.type(), k -> new HashSet<>());
            try {
                Method method = sig.type().getMethod(sig.method(), sig.args());
                methods.add(method);
            } catch (NoSuchMethodException e) {
                throw new PluginException("Could not find method on " + sig.type() + " named " + sig.method() + ". Cause: " + e, e);
            }
        }
        System.out.println(signatureMap);
    }
}
