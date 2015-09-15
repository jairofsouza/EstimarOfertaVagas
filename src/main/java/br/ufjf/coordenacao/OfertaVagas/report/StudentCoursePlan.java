package br.ufjf.coordenacao.OfertaVagas.report;

import java.util.HashMap;
import java.util.TreeSet;
import br.ufjf.coordenacao.OfertaVagas.model.Class;
import br.ufjf.coordenacao.OfertaVagas.model.ClassStatus;
import br.ufjf.coordenacao.OfertaVagas.model.Curriculum;
import br.ufjf.coordenacao.OfertaVagas.model.Student;

public class StudentCoursePlan {
	
	private HashMap<Integer, TreeSet<Class>> _period;
	private int[] _periodWorkload;
	private int _maxWorkload;
	private int _currentPeriod;
	private Student _student;
	private HashMap<Integer, TreeSet<Class>> _notCompleted;
	
	//Construtor recebe o aluno, a grade de disciplinas e o maximo de creditos por periodo
	public StudentCoursePlan(Student st, Curriculum cur, int maxWorkload)
	{
		_maxWorkload = maxWorkload;
		_currentPeriod = 0;
		_student = st;
		_period = new HashMap<Integer, TreeSet<Class>>();
		_notCompleted = new HashMap<Integer, TreeSet<Class>>(); 
		
		//Retira do curriculum as disciplinas que ainda nao forma cursadas
		int courseCount = 0;
		for(int i: cur.getMandatories().keySet())
		{
			TreeSet<Class> t = new TreeSet<Class>();
			for(Class c: cur.getMandatories().get(i))
			{			
				if(!st.getClasses(ClassStatus.APPROVED).containsKey(c) && !st.getClasses(ClassStatus.ENROLLED).containsKey(c))
				{
					t.add(c);
					courseCount++;
				}
			}
			if(!t.isEmpty())
			{
				_notCompleted.put(i, t);
			}
		}
		
		_periodWorkload = new int[courseCount];
	}
	
	//Verifica qual o periodo minimo que o aluno pode cursar aquela disciplina
	private int checkPrereq(Class c)
	{
		//Se ele ja esteve matriculado nela antes, entao ele pode cursar no primeiro periodo livre
		if(_student.getClasses(ClassStatus.REPROVED_GRADE).containsKey(c) || _student.getClasses(ClassStatus.REPROVED_FREQUENCY).containsKey(c))
			return _currentPeriod;
		
		//Caso nao, ele verifica o maior periodo dos prerequisitos e compara com o primeiro periodo livre, e retorna o maior.\
		int i = 0;
		for(Class cl: c.getPrerequisite())
		{
			if(!_student.getClasses(ClassStatus.APPROVED).containsKey(cl) || _student.getClasses(ClassStatus.ENROLLED).containsKey(cl))
			{
				for(int p :_period.keySet())
				{
					if(_period.get(p).contains(cl))
						if(p>i)
						{
							i = p;
							if(i >= _currentPeriod)
								i++;
						}
				}
			}
		}
		return (_currentPeriod > i ? _currentPeriod : i);
	}
	
	public void add(Class c, boolean par)
	{
		int period = checkPrereq(c);
		
		if(par)
		{
			if(period % 2 != 0)
				period++;
		}
		else
		{
			if(period % 2 == 0)
				period++;
		}
		
		
		while((_periodWorkload[period] + c.getWorkload() > _maxWorkload) && (_periodWorkload[period] != 0))
			period++;
		
		_periodWorkload[period] += c.getWorkload();
		
		if(_period.containsKey(period))
			_period.get(period).add(c);
		else
		{
			TreeSet<Class> t = new TreeSet<Class>();
			t.add(c);
			_period.put(period, t);
		}
		
		if(_periodWorkload[_currentPeriod] == _maxWorkload)
			_currentPeriod++;
	}
	
	public Curriculum generate()
	{	
		
		for(int i: _notCompleted.keySet())
		{
			
			boolean par = (i % 2 == 0);
			
			for(Class cl: _notCompleted.get(i))
				add(cl, par);
		}
		
		Curriculum cur = new Curriculum(_period, new TreeSet<Class>());
	
		return cur;
	}
	
}
