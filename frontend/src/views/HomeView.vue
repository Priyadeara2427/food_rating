<template>
  <div class="home">
    <div class="welcome-section">
      <h2>Welcome to Canteen Menu & Ratings</h2>
      <p>Discover daily dishes and rate your favorites from different canteens</p>
    </div>

    <div class="header-actions">
      <button @click="goToAddDish" class="btn-primary">+ Add New Dish</button>
    </div>
    
    <div class="canteen-list">
      <h3>Available Canteens</h3>
      <div v-if="loading" class="loading">Loading canteens...</div>
      <div v-else-if="canteens.length === 0" class="no-data">
        No canteens available. Please add some canteens first.
      </div>
      <div v-else class="canteen-grid">
        <div v-for="canteen in canteens" :key="canteen.id" class="canteen-card">
          <h4>{{ canteen.name }}</h4>
          <p class="location">📍 {{ canteen.location }}</p>
          <p class="description">{{ canteen.description }}</p>
          <div class="dish-count">
            {{ canteen.dishes ? canteen.dishes.length : 0 }} dishes available
          </div>
          <button @click="viewCanteen(canteen.id)" class="btn-secondary">View Menu & Rate</button>
        </div>
      </div>
    </div>

    <!-- Recently Viewed using Local Storage -->
    <div v-if="recentlyViewed.length > 0" class="recently-viewed">
      <h3>Recently Viewed Canteens</h3>
      <div class="recent-items">
        <div v-for="item in recentlyViewed" :key="item.id" 
             @click="viewCanteen(item.id)" class="recent-item">
          {{ item.name }}
        </div>
      </div>
    </div>

    <!-- Quick Stats using Session Storage -->
    <div class="quick-stats">
      <h3>Your Session</h3>
      <div class="stats-grid">
        <div class="stat-card">
          <span class="stat-number">{{ sessionStats.canteensViewed }}</span>
          <span class="stat-label">Canteens Viewed</span>
        </div>
        <div class="stat-card">
          <span class="stat-number">{{ sessionStats.dishesRated }}</span>
          <span class="stat-label">Dishes Rated</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'HomeView',
  data() {
    return {
      canteens: [],
      recentlyViewed: [],
      loading: false,
      sessionStats: {
        canteensViewed: 0,
        dishesRated: 0
      }
    }
  },
  mounted() {
    this.fetchCanteens()
    this.loadRecentlyViewed()
    this.loadSessionStats()
  },
  methods: {
    async fetchCanteens() {
      this.loading = true
      try {
        const response = await fetch('http://localhost:8080/api/canteens')
        if (response.ok) {
          this.canteens = await response.json()
        } else {
          console.error('Failed to fetch canteens')
        }
      } catch (error) {
        console.error('Error fetching canteens:', error)
        this.showError('Failed to load canteens. Please check if the backend is running.')
      } finally {
        this.loading = false
      }
    },
    viewCanteen(canteenId) {
      const canteen = this.canteens.find(c => c.id === canteenId)
      if (canteen) {
        this.addToRecentlyViewed(canteen)
        this.incrementSessionStat('canteensViewed')
        this.$router.push({ name: 'canteen', params: { id: canteenId } })
      }
    },
    goToAddDish() {
      this.$router.push({ name: 'add-dish' })
    },
    addToRecentlyViewed(canteen) {
      let recent = JSON.parse(localStorage.getItem('recentlyViewedCanteens') || '[]')
      recent = recent.filter(item => item.id !== canteen.id)
      recent.unshift({
        id: canteen.id,
        name: canteen.name,
        timestamp: new Date().toISOString()
      })
      if (recent.length > 5) recent = recent.slice(0, 5)
      localStorage.setItem('recentlyViewedCanteens', JSON.stringify(recent))
      this.recentlyViewed = recent
    },
    loadRecentlyViewed() {
      this.recentlyViewed = JSON.parse(localStorage.getItem('recentlyViewedCanteens') || '[]')
    },
    loadSessionStats() {
      const stats = sessionStorage.getItem('sessionStats')
      if (stats) {
        this.sessionStats = JSON.parse(stats)
      }
    },
    incrementSessionStat(statName) {
      this.sessionStats[statName]++
      sessionStorage.setItem('sessionStats', JSON.stringify(this.sessionStats))
    },
    showError(message) {
      alert(message) // In a real app, use a proper notification system
    }
  }
}
</script>

<style scoped>
.home {
  max-width: 1000px;
  margin: 0 auto;
}

.welcome-section {
  text-align: center;
  margin-bottom: 2rem;
  padding: 2rem;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  border-radius: 10px;
}

.welcome-section h2 {
  color: #2c3e50;
  margin-bottom: 0.5rem;
}

.welcome-section p {
  color: #7f8c8d;
  font-size: 1.1rem;
}

.header-actions {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 2rem;
}

.canteen-list h3 {
  margin-bottom: 1rem;
  color: #2c3e50;
}

.loading, .no-data {
  text-align: center;
  padding: 2rem;
  color: #7f8c8d;
  font-style: italic;
}

.canteen-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 1.5rem;
  margin-bottom: 3rem;
}

.canteen-card {
  background: white;
  border: 1px solid #e1e8ed;
  border-radius: 10px;
  padding: 1.5rem;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
  transition: transform 0.2s, box-shadow 0.2s;
}

.canteen-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 20px rgba(0,0,0,0.15);
}

.canteen-card h4 {
  color: #2c3e50;
  margin-bottom: 0.5rem;
}

.location {
  color: #e74c3c;
  font-weight: bold;
  margin-bottom: 0.5rem;
}

.description {
  color: #7f8c8d;
  margin-bottom: 1rem;
  line-height: 1.4;
}

.dish-count {
  background: #ecf0f1;
  padding: 0.5rem;
  border-radius: 5px;
  text-align: center;
  margin-bottom: 1rem;
  font-weight: bold;
  color: #34495e;
}

.recently-viewed, .quick-stats {
  background: #f8f9fa;
  padding: 1.5rem;
  border-radius: 10px;
  margin-bottom: 2rem;
}

.recently-viewed h3, .quick-stats h3 {
  margin-bottom: 1rem;
  color: #2c3e50;
}

.recent-items {
  display: flex;
  gap: 1rem;
  flex-wrap: wrap;
}

.recent-item {
  background: white;
  padding: 0.75rem 1rem;
  border-radius: 20px;
  border: 2px solid #3498db;
  color: #3498db;
  cursor: pointer;
  transition: all 0.3s;
  font-weight: bold;
}

.recent-item:hover {
  background: #3498db;
  color: white;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
  gap: 1rem;
}

.stat-card {
  background: white;
  padding: 1.5rem;
  border-radius: 10px;
  text-align: center;
  border-left: 4px solid #3498db;
}

.stat-number {
  display: block;
  font-size: 2rem;
  font-weight: bold;
  color: #2c3e50;
}

.stat-label {
  color: #7f8c8d;
  font-size: 0.9rem;
}

.btn-primary {
  background: #3498db;
  color: white;
  border: none;
  padding: 0.75rem 1.5rem;
  border-radius: 5px;
  cursor: pointer;
  font-size: 1rem;
  transition: background-color 0.3s;
}

.btn-primary:hover {
  background: #2980b9;
}

.btn-secondary {
  background: transparent;
  color: #3498db;
  border: 2px solid #3498db;
  padding: 0.5rem 1rem;
  border-radius: 5px;
  cursor: pointer;
  transition: all 0.3s;
  width: 100%;
}

.btn-secondary:hover {
  background: #3498db;
  color: white;
}

@media (max-width: 768px) {
  .canteen-grid {
    grid-template-columns: 1fr;
  }
  
  .header-actions {
    justify-content: center;
  }
  
  .recent-items {
    justify-content: center;
  }
}
</style>