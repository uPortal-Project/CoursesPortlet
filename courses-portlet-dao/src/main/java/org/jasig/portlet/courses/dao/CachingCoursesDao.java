package org.jasig.portlet.courses.dao;

import java.io.Serializable;

import javax.portlet.PortletRequest;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.constructs.blocking.CacheEntryFactory;
import net.sf.ehcache.constructs.blocking.SelfPopulatingCache;

import org.jasig.portlet.courses.model.xml.TermList;
import org.jasig.portlet.courses.model.xml.personal.CoursesByTerm;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.annotation.Required;

/**
 * Wraps a {@link ICacheableCoursesDao} and presents it as a {@link ICoursesDao} adding caching
 * 
 * @author Eric Dalquist
 */
public final class CachingCoursesDao implements ICoursesDao, BeanNameAware {
    private ICacheableCoursesDao<Serializable, Serializable> coursesDao;
    private SelfPopulatingCache termListCache;
    private SelfPopulatingCache coursesByTermCache;
    private boolean scopeKeysToDao = true;
    private String beanName; 
    
    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }

    /**
     * @param coursesDao The dao to add caching to
     */
    public void setEnclosedCoursesDao(ICacheableCoursesDao<Serializable, Serializable> coursesDao) {
        this.coursesDao = coursesDao;
    }
    
    /**
     * @param scopeKeysToDao If generated cache key should be scoped to this caching dao instance, defaults to true.
     */
    public void setScopeKeysToDao(boolean scopeKeysToDao) {
        this.scopeKeysToDao = scopeKeysToDao;
    }

    /**
     * @param termListCache The cache to use for term lists
     */
    @Required
    public void setTermListCache(Ehcache termListCache) {
        this.termListCache = new SelfPopulatingCache(termListCache, new CacheEntryFactory() {
            @Override
            public Object createEntry(Object key) throws Exception {
                final CacheKey scopedKey = (CacheKey)key;
                return coursesDao.getTermList(scopedKey.key);
            }
        });
    }
    
    /**
     * @param coursesByTermCache The cache to use for courses by term
     */
    @Required
    public void setCoursesByTermCache(Ehcache coursesByTermCache) {
        this.coursesByTermCache = new SelfPopulatingCache(coursesByTermCache, new CacheEntryFactory() {
            @Override
            public Object createEntry(Object key) throws Exception {
                final CacheKey scopedKey = (CacheKey)key;
                return coursesDao.getCoursesByTerm(scopedKey.key);
            }
        });
    }
    
    @Override
    public TermList getTermList(PortletRequest request) {
        final Serializable termListKey = coursesDao.getTermListKey(request);
        final CacheKey scopedKey = getScopedKey(termListKey);
        
        final Element element = this.termListCache.get(scopedKey);
        if (element == null) { 
            return null;
        }
        
        return (TermList)element.getObjectValue();
    }

    @Override
    public CoursesByTerm getCoursesByTerm(PortletRequest request, String termCode) {
        final TermList termList = this.getTermList(request);
        final Serializable coursesByTermKey = coursesDao.getCoursesByTermKey(request, termCode, termList);
        final CacheKey scopedKey = getScopedKey(coursesByTermKey);

        final Element element = this.coursesByTermCache.get(scopedKey);
        if (element == null) { 
            return null;
        }
        
        return (CoursesByTerm)element.getObjectValue();
    }
    
    protected CacheKey getScopedKey(Serializable key) {
        if (this.scopeKeysToDao) {
            return new CacheKey(this.beanName, key);
        }
        
        return new CacheKey(key);
    }
    
    private static final class CacheKey implements Serializable {
        private static final long serialVersionUID = 1L;
        
        private final String scope;
        private final Serializable key;
        
        public CacheKey(Serializable key) {
            this.scope = null;
            this.key = key;
        }
        
        public CacheKey(String scope, Serializable key) {
            this.scope = scope;
            this.key = key;
        }
        
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((key == null) ? 0 : key.hashCode());
            result = prime * result + ((scope == null) ? 0 : scope.hashCode());
            return result;
        }
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            CacheKey other = (CacheKey) obj;
            if (key == null) {
                if (other.key != null)
                    return false;
            }
            else if (!key.equals(other.key))
                return false;
            if (scope == null) {
                if (other.scope != null)
                    return false;
            }
            else if (!scope.equals(other.scope))
                return false;
            return true;
        }

        @Override
        public String toString() {
            return "ScopedCacheKey [scope=" + scope + ", key=" + key + "]";
        }
    }
}
