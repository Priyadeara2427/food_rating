<template>
  <div class="edit-dish-view">
    <div class="header">
      <button @click="$router.back()" class="back-btn">← Back</button>
      <h2>Edit Dish</h2>
    </div>

    <div class="form-container">
      <form @submit.prevent="submitForm" class="dish-form">
        <div class="form-group">
          <label for="name">Dish Name *</label>
          <input 
            type="text" 
            id="name" 
            v-model="form.name"
            @blur="validateField('name')"
            :class="{ 'error': errors.name }"
            placeholder="Enter dish name"
            required
          >
          <span v-if="errors.name" class="error-message">{{ errors.name }}</span>
        </div>

        <div class="form-group">
          <label for="description">Description *</label>
          <textarea 
            id="description" 
            v-model="form.description"
            @blur="validateField('description')"
            :class="{ 'error': errors.description }"
            placeholder="Describe the dish..."
            rows="3"
            required
          ></textarea>
          <span v-if="errors.description" class="error-message">{{ errors.description }}</span>
        </div>

        <div class="form-row">
          <div class="form-group">
            <label for="category">Category *</label>
            <select 
              id="category" 
              v-model="form.category"
              @blur="validateField('category')"
              :class="{ 'error': errors.category }"
              required
            >
              <option value="">Select category</option>
              <option value="Appetizer">Appetizer</option>
              <option value="Main Course">Main Course</option>
              <option value="Dessert">Dessert</option>
              <option value="Beverage">Beverage</option>
              <option value="Snack">Snack</option>
              <option value="Healthy">Healthy</option>
              <option value="Vegetarian">Vegetarian</option>
              <option value="Non-Vegetarian">Non-Vegetarian</option>
            </select>
            <span v-if="errors.category" class="error-message">{{ errors.category }}</span>
          </div>

          <div class="form-group">
            <label for="price">Price (₹) *</label>
            <input 
              type="number" 
              id="price" 
              v-model="form.price"
              @blur="validateField('price')"
              :class="{ 'error': errors.price }"
              min="0"
              step="0.01"
              placeholder="0.00"
              required
            >
            <span v-if="errors.price" class="error-message">{{ errors.price }}</span>
          </div>
        </div>

        <div class="form-group">
          <label for="rating">Rating (1-5) *</label>
          <select 
            id="rating" 
            v-model="form.rating"
            @blur="validateField('rating')"
            :class="{ 'error': errors.rating }"
            required
          >
            <option value="">Select rating</option>
            <option v-for="n in 5" :key="n" :value="n">{{ n }} ⭐</option>
          </select>
          <span v-if="errors.rating" class="error-message">{{ errors.rating }}</span>
        </div>

        <div class="form-actions">
          <button type="button" @click="$router.back()" class="btn-cancel">Cancel</button>
          <button type="button" @click="deleteDish" class="btn-delete" :disabled="submitting">
            Delete Dish
          </button>
          <button type="submit" :disabled="!isFormValid || submitting" class="btn-submit">
            {{ submitting ? 'Updating Dish...' : 'Update Dish' }}
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
export default {
  name: 'EditDishView',
  data() {
    return {
      dish: null,
      form: {
        name: '',
        description: '',
        category: '',
        price: 0,
        rating: null
      },
      errors: {},
      submitting: false,
      loading: false
    }
  },
  computed: {
    isFormValid() {
      return this.form.name && 
             this.form.description && 
             this.form.category && 
             this.form.price > 0 &&
             this.form.rating
    }
  },
  mounted() {
    this.fetchDishDetails()
  },
  methods: {
    async fetchDishDetails() {
      this.loading = true
      const dishId = this.$route.params.id
      
      try {
        const response = await fetch(`http://localhost:8080/api/dishes/${dishId}`)
        if (response.ok) {
          this.dish = await response.json()
          // Populate form with existing data
          this.form = {
            name: this.dish.name,
            description: this.dish.description,
            category: this.dish.category,
            price: this.dish.price,
            rating: this.dish.rating
          }
        } else {
          this.showError('Dish not found')
          this.$router.back()
        }
      } catch (error) {
        console.error('Error fetching dish:', error)
        this.showError('Failed to load dish details')
        this.$router.back()
      } finally {
        this.loading = false
      }
    },
    validateField(field) {
      this.errors[field] = ''
      
      switch(field) {
        case 'name':
          if (!this.form.name) this.errors.name = 'Dish name is required'
          else if (this.form.name.length < 2) this.errors.name = 'Name must be at least 2 characters'
          break
        case 'description':
          if (!this.form.description) this.errors.description = 'Description is required'
          else if (this.form.description.length > 500) this.errors.description = 'Description cannot exceed 500 characters'
          break
        case 'category':
          if (!this.form.category) this.errors.category = 'Category is required'
          break
        case 'price':
          if (!this.form.price || this.form.price <= 0) this.errors.price = 'Price must be greater than 0'
          break
        case 'rating':
          if (!this.form.rating) this.errors.rating = 'Rating is required'
          break
      }
    },
    async submitForm() {
      // Validate all fields
      Object.keys(this.form).forEach(field => this.validateField(field))
      
      if (!this.isFormValid) {
        this.showError('Please fix all errors before submitting')
        return
      }

      this.submitting = true

      try {
        const dishData = {
          name: this.form.name.trim(),
          description: this.form.description.trim(),
          category: this.form.category,
          price: parseFloat(this.form.price),
          rating: parseInt(this.form.rating)
        }

        console.log('Updating dish data:', dishData);

        const response = await fetch(`http://localhost:8080/api/dishes/${this.dish.id}`, {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(dishData)
        })

        console.log('Response status:', response.status);

        if (response.ok) {
          const result = await response.json();
          console.log('Success response:', result);
          
          this.showSuccess('Dish updated successfully!')
          // Navigate back to canteen page
          this.$router.push({ 
            name: 'canteen', 
            params: { id: this.dish.canteen.id.toString() }
          })
        } else {
          let errorMessage = 'Failed to update dish';
          try {
            const errorData = await response.json();
            errorMessage += ': ' + (errorData.message || errorData || 'Unknown error');
          } catch (e) {
            errorMessage += `: HTTP ${response.status} - ${response.statusText}`;
          }
          this.showError(errorMessage)
        }
      } catch (error) {
        console.error('Error updating dish:', error)
        this.showError('Failed to update dish: ' + error.message)
      } finally {
        this.submitting = false
      }
    },
    async deleteDish() {
      if (!confirm('Are you sure you want to delete this dish? This action cannot be undone.')) {
        return
      }

      this.submitting = true

      try {
        const response = await fetch(`http://localhost:8080/api/dishes/${this.dish.id}`, {
          method: 'DELETE'
        })

        if (response.ok) {
          this.showSuccess('Dish deleted successfully!')
          // Navigate back to canteen page
          this.$router.push({ 
            name: 'canteen', 
            params: { id: this.dish.canteen.id.toString() }
          })
        } else {
          let errorMessage = 'Failed to delete dish';
          try {
            const errorData = await response.json();
            errorMessage += ': ' + (errorData.message || errorData || 'Unknown error');
          } catch (e) {
            errorMessage += `: HTTP ${response.status} - ${response.statusText}`;
          }
          this.showError(errorMessage)
        }
      } catch (error) {
        console.error('Error deleting dish:', error)
        this.showError('Failed to delete dish: ' + error.message)
      } finally {
        this.submitting = false
      }
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
.edit-dish-view {
  max-width: 600px;
  margin: 0 auto;
}

.header {
  display: flex;
  align-items: center;
  gap: 1rem;
  margin-bottom: 2rem;
}

.back-btn {
  background: none;
  border: none;
  color: #3498db;
  cursor: pointer;
  font-size: 1rem;
  padding: 0.5rem;
}

.back-btn:hover {
  text-decoration: underline;
}

.header h2 {
  color: #2c3e50;
  margin: 0;
}

.form-container {
  background: white;
  padding: 2rem;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
}

.dish-form {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.form-group {
  display: flex;
  flex-direction: column;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
}

label {
  font-weight: bold;
  margin-bottom: 0.5rem;
  color: #2c3e50;
}

input, select, textarea {
  padding: 0.75rem;
  border: 2px solid #bdc3c7;
  border-radius: 5px;
  font-size: 1rem;
  transition: border-color 0.3s;
}

input:focus, select:focus, textarea:focus {
  outline: none;
  border-color: #3498db;
}

input.error, select.error, textarea.error {
  border-color: #e74c3c;
}

.error-message {
  color: #e74c3c;
  font-size: 0.9rem;
  margin-top: 0.25rem;
}

.form-actions {
  display: flex;
  gap: 1rem;
  justify-content: space-between;
  margin-top: 2rem;
  padding-top: 1rem;
  border-top: 1px solid #ecf0f1;
}

.btn-cancel, .btn-delete, .btn-submit {
  padding: 0.75rem 1.5rem;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 1rem;
  transition: background-color 0.3s;
}

.btn-cancel {
  background: #95a5a6;
  color: white;
}

.btn-cancel:hover {
  background: #7f8c8d;
}

.btn-delete {
  background: #e74c3c;
  color: white;
}

.btn-delete:hover:not(:disabled) {
  background: #c0392b;
}

.btn-delete:disabled {
  background: #bdc3c7;
  cursor: not-allowed;
}

.btn-submit {
  background: #3498db;
  color: white;
}

.btn-submit:hover:not(:disabled) {
  background: #2980b9;
}

.btn-submit:disabled {
  background: #bdc3c7;
  cursor: not-allowed;
}

@media (max-width: 768px) {
  .form-row {
    grid-template-columns: 1fr;
  }
  
  .form-actions {
    flex-direction: column;
  }
  
  .btn-cancel, .btn-delete, .btn-submit {
    width: 100%;
  }
}
</style>