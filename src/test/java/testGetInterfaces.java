import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class testGetInterfaces
{

	public interface A
	{
		String getBar();
	}

	public interface A2 extends A
	{
	}

	public interface A3 extends A2
	{
		String getBaz();
	}

	public interface A4
	{

	}

	public abstract class C implements A
	{
		public abstract String getFoo();
	}

	public abstract class C2 extends C
	{
		@Override
		public String getBar()
		{
			return "bar";
		}

		@Override
		public String getFoo()
		{
			return "foo";
		}
	}

	public abstract class C3 extends C2
	{

	}

	public class D extends C3 implements A3
	{
		@Override
		public String getBaz()
		{
			return "baz";
		}
	}

	public class E extends D implements A
	{

	}

	public abstract class F extends E implements A4
	{
		@Override
		abstract public String getBaz();

	}

	public abstract class G extends C2 implements A4, A3
	{

	}

	public static void main( final String[] args )
	{
		final testGetInterfaces tgi = new testGetInterfaces();

		final Class<?>[] clsLst = { D.class, E.class, F.class, G.class };
		for (final Class<?> cls : clsLst)
		{
			System.out.println(String.format("%s classes", cls));
			for (final Class<?> c : tgi.findInterfaces(cls))
			{
				System.out.println(c.getName());
			}
		}

		final Set<Class<?>> s = tgi.findInterfaces(F.class);

		List<Method> lm = tgi.findMethod(s, "getFoo");
		for (final Method m : lm)
		{
			System.out.println(String.format("%s.%s", m.getDeclaringClass(),
					m.getName()));
		}

		lm = tgi.findMethod(s, "getBar");
		for (final Method m : lm)
		{
			System.out.println(String.format("%s.%s", m.getDeclaringClass(),
					m.getName()));
		}

		lm = tgi.findMethod(s, "getBaz");
		for (final Method m : lm)
		{
			System.out.println(String.format("%s.%s", m.getDeclaringClass(),
					m.getName()));
		}

	}

	public Set<Class<?>> findInterfaces( final Class<?> clazz )
	{

		if (clazz.equals(Object.class))
		{
			return Collections.emptySet();
		}
		final Queue<Class<?>> queue = new LinkedList<Class<?>>();
		queue.offer(clazz);
		// order preserving set
		final Set<Class<?>> interfaces = new LinkedHashSet<Class<?>>();

		for (Class<?> cls = queue.poll(); cls != null; cls = queue.poll())
		{

			if (!cls.equals(Object.class))
			{
				if (Modifier.isAbstract(cls.getModifiers()))
				{
					interfaces.add(cls);
				}
				final Class<?> sc = cls.getSuperclass();
				if ((sc != null) && !interfaces.contains(sc)
						&& !queue.contains(sc))
				{
					queue.offer(sc);
				}
			}
			for (final Class<?> c : cls.getInterfaces())
			{
				if (!interfaces.contains(c) && !queue.contains(c))
				{
					// interfaces.add(c);
					queue.offer(c);
				}
			}

		}
		return interfaces;
	}

	public List<Method> findMethod( final Set<Class<?>> s,
			final String methodName )
	{
		final List<Method> lst = new ArrayList<Method>();
		for (final Class<?> c : s)
		{
			for (final Method m : c.getDeclaredMethods())
			{
				if (m.getName().equals(methodName)
						&& Modifier.isAbstract(m.getModifiers()))
				{
					lst.add(m);
				}
			}
		}
		return lst;
	}
}
