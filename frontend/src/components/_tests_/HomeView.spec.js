import { describe, it, expect, beforeEach, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import HomeView from '../../views/HomeView.vue'
import { createRouter, createWebHistory } from 'vue-router'

// Mock fetch
global.fetch = vi.fn()

// Mock localStorage
const localStorageMock = {
  getItem: vi.fn(),
  setItem: vi.fn(),
  clear: vi.fn()
}
global.localStorage = localStorageMock

// Mock sessionStorage
const sessionStorageMock = {
  getItem: vi.fn(),
  setItem: vi.fn(),
  clear: vi.fn()
}
global.sessionStorage = sessionStorageMock

describe('HomeView', () => {
  let wrapper
  let router

  beforeEach(async () => {
    router = createRouter({
      history: createWebHistory(),
      routes: [
        { path: '/', component: HomeView },
        { path: '/canteen/:id', component: { template: '<div></div>' } },
        { path: '/add-dish', component: { template: '<div></div>' } }
      ]
    })

    // Mock fetch response
    fetch.mockResolvedValue({
      ok: true,
      json: () => Promise.resolve([
        { 
          id: 1, 
          name: 'Test Canteen', 
          location: 'Test Location',
          description: 'Test Description',
          dishes: []
        }
      ])
    })

    // Mock localStorage
    localStorage.getItem.mockReturnValue(JSON.stringify([]))
    sessionStorage.getItem.mockReturnValue(JSON.stringify({ canteensViewed: 0, dishesRated: 0 }))

    wrapper = mount(HomeView, {
      global: {
        plugins: [router],
        stubs: ['router-link']
      }
    })

    await router.isReady()
  })

  it('renders home view with correct title', () => {
    expect(wrapper.text()).toContain('Canteen Menu & Ratings')
  })

  it('displays canteen list when data is loaded', async () => {
    await wrapper.vm.$nextTick()
    expect(wrapper.text()).toContain('Test Canteen')
    expect(wrapper.text()).toContain('Test Location')
  })

  it('has add dish button', () => {
    expect(wrapper.text()).toContain('Add New Dish')
  })

  it('shows loading state initially', () => {
    expect(wrapper.text()).toContain('Loading canteens...')
  })

  it('handles fetch errors gracefully', async () => {
    fetch.mockRejectedValueOnce(new Error('Network error'))
    
    const errorWrapper = mount(HomeView, {
      global: {
        plugins: [router],
        stubs: ['router-link']
      }
    })
    
    await errorWrapper.vm.$nextTick()
    // Should handle error without crashing
    expect(errorWrapper.exists()).toBe(true)
  })
})