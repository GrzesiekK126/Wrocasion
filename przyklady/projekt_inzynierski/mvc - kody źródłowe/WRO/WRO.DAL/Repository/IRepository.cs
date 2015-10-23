using System;
using System.Collections.Generic;
using System.Linq;
using System.Linq.Expressions;
using System.Text;
using System.Threading.Tasks;

namespace WRO.DAL.Repository
{
    public interface IRepository<T> where T : class
    {
        int Count();

        void Add(T entity);

        void Delete(T entity);

        void Update(T entity);

        T FindBy(Expression<Func<T, bool>> predicate, params Expression<Func<T, object>>[] includeProperties);

        IQueryable<T> GetBy(Expression<Func<T, bool>> predicate, params Expression<Func<T, object>>[] includeProperties);

        IQueryable<T> GetAll(params Expression<Func<T, object>>[] includeProperties);
    }
}
