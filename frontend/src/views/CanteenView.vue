<template>
  <div class="canteen-view">
    <div v-if="loading" class="loading">Loading canteen details...</div>
    <div v-else-if="canteen" class="canteen-details">
      <div class="canteen-header">
        <button @click="$router.back()" class="back-btn">← Back</button>
        <h2>{{ canteen.name }}</h2>
        <p class="location">📍 {{ canteen.location }}</p>
        <p class="description">{{ canteen.description }}</p>
      </div>

      <div class="dishes-section">
        <div class="section-header">
          <h3>Daily Menu</h3>
          <button @click="goToAddDish" class="btn-primary">+ Add Dish to this Canteen</button>
        </div>

        <div v-if="dishesLoading" class="loading">Loading dishes...</div>
        <div v-else-if="dishes.length === 0" class="no-dishes">
          <p>No dishes available for this canteen yet.</p>
          <button @click="goToAddDish" class="btn-primary">Add First Dish</button>
        </div>
        <div v-else class="dishes-grid">
          <div v-for="dish in dishes" :key="dish.id" class="dish-card">
            <div class="dish-header">
              <h4>{{ dish.name }}</h4>
              <div class="rating">
                <span class="stars">{{ '⭐'.repeat(dish.rating) }}</span>
                <span class="rating-number">({{ dish.rating }}/5)</span>
              </div>
            </div>
            <p class="dish-description">{{ dish.description }}</p>
            <div class="dish-details">
              <span class="category">Category: {{ dish.category }}</span>
              <span class="price">${{ dish.price.toFixed(2) }}</span>
            </div>
            <div class="dish-actions">
              <button @click="rateDish(dish.id)" class="btn-rate">Rate This Dish</button>
              <button @click="editDish(dish.id)" class="btn-edit">Edit</button>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div v-else class="error">
      Canteen not found.
    </div>

    <!-- Rating Modal -->
    <div v-if="showRatingModal" class="modal-overlay">
      <div class="modal">
        <h3>Rate {{ selectedDish?.name }}</h3>
        <div class="rating-options">
          <button 
            v-for="rating in 5" 
            :key="rating" 
            @click="submitRating(rating)"
            class="rating-option"
            :class="{ active: tempRating === rating }"
          >
            {{ rating }} ⭐
          </button>
        </div>
        <div class="modal-actions">
          <button @click="showRatingModal = false" class="btn-cancel">Cancel</button>
          <button @click="submitRating(tempRating)" class="btn-confirm" :disabled="!tempRating">
            Submit Rating
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'CanteenView',
  props: ['id'],
  data() {
    return {
      canteen: null,
      dishes: [],
      loading: false,
      dishesLoading: false,
      showRatingModal: false,
      selectedDish: null,
      tempRating: 0
    }
  },
  mounted() {
    this.fetchCanteenDetails()
    this.fetchDishes()
  },
  methods: {
    async fetchCanteenDetails() {
      this.loading = true
      try {
        const response = await fetch(`http://localhost:8080/api/canteens/${this.id}`)
        if (response.ok) {
          this.canteen = await response.json()
        } else {
          this.showError('Canteen not found')
        }
      } catch (error) {
        console.error('Error fetching canteen:', error)
        this.showError('Failed to load canteen details')
      } finally {
        this.loading = false
      }
    },
    async fetchDishes() {
      this.dishesLoading = true
      try {
        const response = await fetch(`http://localhost:8080/api/dishes/canteen/${this.id}`)
        if (response.ok) {
          this.dishes = await response.json()
        }
      } catch (error) {
        console.error('Error fetching dishes:', error)
        this.showError('Failed to load dishes')
      } finally {
        this.dishesLoading = false
      }
    },
    rateDish(dishId) {
      this.selectedDish = this.dishes.find(d => d.id === dishId)
      this.tempRating = this.selectedDish.rating
      this.showRatingModal = true
    },
    async submitRating(rating) {
      if (!this.selectedDish) return

      try {
        const response = await fetch(`http://localhost:8080/api/dishes/${this.selectedDish.id}`, {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            ...this.selectedDish,
            rating: rating
          })
        })

        if (response.ok) {
          const updatedDish = await response.json()
          // Update local state
          const index = this.dishes.findIndex(d => d.id === updatedDish.id)
          if (index !== -1) {
            this.dishes[index] = updatedDish
          }
          this.showRatingModal = false
          this.tempRating = 0
          this.selectedDish = null
          
          // Update session stats
          this.incrementSessionStat('dishesRated')
          this.showSuccess('Rating submitted successfully!')
        } else {
          this.showError('Failed to update rating')
        }
      } catch (error) {
        console.error('Error updating rating:', error)
        this.showError('Failed to submit rating')
      }
    },
    editDish(dishId) {
      // Navigate to edit dish page or show edit modal
      this.showError('Edit functionality not implemented yet')
    },
    goToAddDish() {
      this.$router.push({ 
        name: 'add-dish',
        query: { canteenId: this.id }
      })
    },
    incrementSessionStat(statName) {
      const stats = JSON.parse(sessionStorage.getItem('sessionStats') || '{"canteensViewed":0,"dishesRated":0}')
      stats[statName] = (stats[statName] || 0) + 1
      sessionStorage.setItem('sessionStats', JSON.stringify(stats))
    },
    showError(message) {
      alert('Error: ' + message)
    },
    showSuccess(message) {
      alert('Success: ' + message)
    }
  }
}
</script>

<style scoped>
.canteen-view {
  max-width: 1000px;
  margin: 0 auto;
}

.canteen-header {
  background: white;
  padding: 2rem;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
  margin-bottom: 2rem;
  text-align: center;
}

.back-btn {
  background: none;
  border: none;
  color: #3498db;
  cursor: pointer;
  font-size: 1rem;
  margin-bottom: 1rem;
}

.back-btn:hover {
  text-decoration: underline;
}

.canteen-header h2 {
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
  font-size: 1.1rem;
}

.dishes-section {
  background: white;
  padding: 2rem;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
  flex-wrap: wrap;
  gap: 1rem;
}

.section-header h3 {
  color: #2c3e50;
  margin: 0;
}

.no-dishes {
  text-align: center;
  padding: 3rem;
  color: #7f8c8d;
}

.no-dishes p {
  margin-bottom: 1rem;
  font-size: 1.1rem;
}

.dishes-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 1.5rem;
}

.dish-card {
  border: 1px solid #e1e8ed;
  border-radius: 10px;
  padding: 1.5rem;
  transition: transform 0.2s, box-shadow 0.2s;
}

.dish-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 20px rgba(0,0,0,0.15);
}

.dish-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 1rem;
}

.dish-header h4 {
  color: #2c3e50;
  margin: 0;
  flex: 1;
}

.rating {
  text-align: right;
}

.stars {
  display: block;
  font-size: 1.2rem;
}

.rating-number {
  font-size: 0.9rem;
  color: #7f8c8d;
}

.dish-description {
  color: #5d6d7e;
  margin-bottom: 1rem;
  line-height: 1.4;
}

.dish-details {
  display: flex;
  justify-content: space-between;
  margin-bottom: 1rem;
  font-size: 0.9rem;
}

.category {
  color: #3498db;
  font-weight: bold;
}

.price {
  color: #27ae60;
  font-weight: bold;
  font-size: 1.1rem;
}

.dish-actions {
  display: flex;
  gap: 0.5rem;
}

.btn-rate, .btn-edit {
  flex: 1;
  padding: 0.5rem;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 0.9rem;
  transition: background-color 0.3s;
}

.btn-rate {
  background: #f39c12;
  color: white;
}

.btn-rate:hover {
  background: #e67e22;
}

.btn-edit {
  background: #bdc3c7;
  color: #2c3e50;
}

.btn-edit:hover {
  background: #95a5a6;
}

/* Modal Styles */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0,0,0,0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal {
  background: white;
  padding: 2rem;
  border-radius: 10px;
  max-width: 400px;
  width: 90%;
}

.modal h3 {
  margin-bottom: 1.5rem;
  text-align: center;
  color: #2c3e50;
}

.rating-options {
  display: flex;
  justify-content: center;
  gap: 0.5rem;
  margin-bottom: 2rem;
  flex-wrap: wrap;
}

.rating-option {
  padding: 0.75rem 1rem;
  border: 2px solid #bdc3c7;
  background: white;
  border-radius: 5px;
  cursor: pointer;
  transition: all 0.3s;
}

.rating-option:hover, .rating-option.active {
  background: #3498db;
  color: white;
  border-color: #3498db;
}

.modal-actions {
  display: flex;
  gap: 1rem;
  justify-content: center;
}

.btn-cancel, .btn-confirm {
  padding: 0.75rem 1.5rem;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 1rem;
}

.btn-cancel {
  background: #e74c3c;
  color: white;
}

.btn-cancel:hover {
  background: #c0392b;
}

.btn-confirm {
  background: #27ae60;
  color: white;
}

.btn-confirm:hover:not(:disabled) {
  background: #229954;
}

.btn-confirm:disabled {
  background: #bdc3c7;
  cursor: not-allowed;
}

.loading, .error {
  text-align: center;
  padding: 3rem;
  font-size: 1.1rem;
}

.error {
  color: #e74c3c;
}

@media (max-width: 768px) {
  .section-header {
    flex-direction: column;
    align-items: stretch;
  }
  
  .dishes-grid {
    grid-template-columns: 1fr;
  }
  
  .dish-header {
    flex-direction: column;
  }
  
  .rating {
    text-align: left;
    margin-top: 0.5rem;
  }
  
  .modal {
    margin: 1rem;
  }
  
  .rating-options {
    gap: 0.25rem;
  }
  
  .rating-option {
    padding: 0.5rem 0.75rem;
    font-size: 0.9rem;
  }
}
</style>