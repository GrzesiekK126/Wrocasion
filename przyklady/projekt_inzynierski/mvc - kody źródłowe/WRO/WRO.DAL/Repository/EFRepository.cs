using System;
using System.Data.Entity;
using System.Collections.Generic;
using System.Linq;
using System.Linq.Expressions;
using System.Text;
using System.Threading.Tasks;
using System.Data.Entity.Validation;

namespace WRO.DAL.Repository
{
    public class EFRepository<T> : IRepository<T> where T : class
    {
        private WRO_DBEntities context = null;

        public EFRepository(WRO_DBEntities db_context)
        {
            context = db_context;
            context.Configuration.LazyLoadingEnabled = false;
        }

        public virtual IQueryable<T> GetAll(params Expression<Func<T, object>>[] includeProperties)
        {
            foreach (var property in includeProperties)
            {
                context.Set<T>().Include(property).Load();
            }

            return context.Set<T>();
        }

        public virtual IQueryable<T> GetBy(Expression<Func<T, bool>> predicate, params Expression<Func<T, object>>[] includeProperties)
        {
            foreach (var property in includeProperties)
            {
                context.Set<T>().Include(property).Load();
            }

            return context.Set<T>().Where(predicate);
        }

        public T FindBy(Expression<Func<T, bool>> predicate, params Expression<Func<T, object>>[] includeProperties)
        {
            foreach (var property in includeProperties)
            {
                context.Set<T>().Include(property).Load();
            }

            return context.Set<T>().FirstOrDefault(predicate);
        }

        public virtual void Add(T entity)
        {
            var type = entity.GetType();
            var property = type.GetProperty("DateCreated");
            if (property != null)
            {
                property.SetValue(entity, DateTime.UtcNow, null);
            }

            property = type.GetProperty("LastUpdated");
            if (property != null)
            {
                property.SetValue(entity, DateTime.UtcNow, null);
            }

            context.Set<T>().Add(entity);
            context.SaveChanges();
           /* try
            {
                context.SaveChanges();
            }
            catch (DbEntityValidationException e)
            {
                foreach (var eve in e.EntityValidationErrors)
                {
                    Console.WriteLine("Entity of type \"{0}\" in state \"{1}\" has the following validation errors:",
                        eve.Entry.Entity.GetType().Name, eve.Entry.State);
                    foreach (var ve in eve.ValidationErrors)
                    {
                        Console.WriteLine("- Property: \"{0}\", Error: \"{1}\"",
                            ve.PropertyName, ve.ErrorMessage);
                    }
                }
                throw;
            }*/
        }

        public virtual void Delete(T entity)
        {
            context.Set<T>().Remove(entity);
            context.SaveChanges();
        }

        public virtual void Update(T entity)
        {
            var type = entity.GetType();
            var property = type.GetProperty("LastUpdated");
            if (property != null)
            {
                property.SetValue(entity, DateTime.UtcNow, null);
            }

            context.Entry(entity).State = EntityState.Modified;
            context.SaveChanges();
        }

        public int Count()
        {
            return context.Set<T>().Count();
        }
    }
}
