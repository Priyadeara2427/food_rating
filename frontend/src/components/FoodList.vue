<template>
  <div class="food-list">
    <h1>Food Listings</h1>
    <div v-for="food in foods" :key="food.id" class="food-item">
      <h3>{{ food.name }}</h3>
      <p>{{ food.description }}</p>
      <span>Category: {{ food.category }}</span>
      <span>Rating: {{ food.averageRating }}/5</span>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      foods: []
    }
  },
  async mounted() {
    await this.fetchFoods();
  },
  methods: {
    async fetchFoods() {
      try {
        const response = await fetch('http://localhost:8080/api/foods');
        this.foods = await response.json();
      } catch (error) {
        console.error('Error fetching foods:', error);
      }
    }
  }
}
</script>

<style>
.food-item {
  border: 1px solid #ddd;
  padding: 1rem;
  margin: 1rem 0;
  border-radius: 5px;
}
</style>